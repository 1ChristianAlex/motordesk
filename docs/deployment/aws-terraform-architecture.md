# Arquitetura Terraform na AWS

Este documento descreve os recursos AWS declarados em `infra/terraform/aws/`, como os ambientes são separados e como
Terraform se integra ao runtime e aos workflows do Motor Desk. Ele documenta a configuração versionada; **não afirma que
os recursos foram criados em uma conta AWS**. A infraestrutura não foi aplicada e ainda depende de configuração e
validação operacional.

## Visão geral

O Terraform mantém uma implementação compartilhada e três roots com state remoto independente:

```text
infra/terraform/aws/
├── backend/                 # Bootstrap do bucket S3 de state
├── envs/
│   ├── dev/                 # Ambiente de desenvolvimento
│   ├── qa/                  # Ambiente de QA
│   └── prod/                # Ambiente de produção
└── modules/
    └── application/         # Recursos compartilhados da aplicação
```

Cada root fixa o ambiente e chama `modules/application`; todos usam chaves próprias de state no S3. Os providers estão
fixados em AWS `6.0.0` e Random `3.7.2`; os roots exigem
Terraform `>= 1.10.0`. A região padrão configurada nos roots é `us-east-2`; o módulo seleciona dinamicamente duas zonas
de disponibilidade disponíveis nessa região.

## State e backend

`backend/` cria o bucket S3 para os states dos ambientes. A configuração declara:

- versionamento habilitado;
- criptografia server-side AES-256;
- bloqueio de acesso público;
- política que nega tráfego sem TLS;
- `force_destroy = false` e `prevent_destroy`, para dificultar a exclusão acidental do bucket.

O bootstrap não usa um backend remoto para o próprio state; seu state inicial é local e precisa ser protegido e
armazenado com segurança pelo operador. O bucket não é criado até executar explicitamente o Terraform com credenciais
AWS autorizadas.

Os roots configuram backend S3 com criptografia e locking nativo via `use_lockfile`:

| Ambiente | Chave de state |
|---|---|
| dev | `motordesk/dev/terraform.tfstate` |
| qa | `motordesk/qa/terraform.tfstate` |
| prod | `motordesk/prod/terraform.tfstate` |

Bucket e região são fornecidos no `terraform init`, e não armazenados no código do root. States e arquivos de plano
podem conter valores sensíveis, incluindo senhas geradas; devem ser tratados como segredos.

## Recursos do módulo `application`

### Rede e entrada HTTP

O módulo cria uma VPC (CIDR padrão `10.40.0.0/16`) nas duas primeiras zonas de disponibilidade disponíveis na região
configurada. Em cada AZ selecionada são criadas:

- uma subnet pública para o Application Load Balancer e NAT;
- uma subnet privada de aplicação para as tarefas ECS;
- uma subnet privada de dados para RDS e ElastiCache.

Um Internet Gateway atende as subnets públicas. As subnets de aplicação enviam tráfego de saída por NAT Gateway; dev e
QA compartilham um NAT Gateway, enquanto produção cria um NAT Gateway por AZ. As subnets de dados não têm rota default
para a internet.

O ALB é público. Seu security group aceita HTTP/80 e HTTPS/443 da internet, e permite saída para a porta 8080 dentro da
VPC. QA e prod, e dev quando `api_domain_name` estiver configurado, redirecionam HTTP para HTTPS usando certificado ACM
gerenciado pelo Terraform. Em dev sem domínio próprio, o DNS gerado pelo ALB encaminha HTTP diretamente para o target
group, sem certificado. A política TLS configurada é `ELBSecurityPolicy-TLS13-1-2-2021-06`.

O target group usa targets do tipo IP e verifica `GET /health` a cada 30 segundos, esperando status 200. O módulo
solicita um certificado ACM para `api_domain_name`, usando validação DNS. Se `route53_zone_id` for preenchido, cria os
registros DNS de validação, espera o certificado ser emitido e também cria um registro A alias para o ALB. Sem uma zona
Route 53, `certificate_validation_records` expõe os CNAMEs necessários para publicação externa. Nesse caso, solicite o
certificado com um apply direcionado, consulte o output, publique os CNAMEs e configure também o alias DNS da API fora
do Terraform. Execute o apply completo após a emissão:

```powershell
terraform -chdir=infra/terraform/aws/envs/dev apply -target=module.application.aws_acm_certificate.api
terraform -chdir=infra/terraform/aws/envs/dev output certificate_validation_records
```

As tarefas ECS não recebem IP público. O security group da aplicação aceita a porta 8080 somente do ALB e libera saída
para tráfego externo através do NAT. Os security groups de PostgreSQL (5432) e Redis (6379) aceitam conexões somente do
security group das tarefas.

### Runtime, imagem e observabilidade

O módulo cria:

- um repositório ECR por ambiente, com tags imutáveis, varredura de imagem no push e criptografia AES-256;
- uma política de ciclo de vida que retém as duas imagens mais recentes;
- um cluster ECS com Container Insights;
- uma task definition e um serviço ECS Fargate da API usando `awsvpc`, CPU padrão de 512 unidades e memória de 1024 MiB;
- uma task definition e um serviço ECS Fargate dedicado ao MongoDB, com uma tarefa;
- um grupo de logs CloudWatch com retenção de 30 dias.

O serviço da API inicia com `desired_count = 0` por padrão, até existir uma imagem inicial no ECR. O workflow de deploy
publica a imagem, registra uma nova revisão da task definition e configura o serviço para uma tarefa em dev/QA ou duas em
prod. O serviço MongoDB inicia com uma tarefa independente da escala da API.
O serviço ECS da API ignora mudanças Terraform em `task_definition` e `desired_count` para que um apply posterior não
reverta a versão ou escala definida pelo deploy.

### Persistência

**Amazon RDS for PostgreSQL**

- PostgreSQL 16.4 por padrão, classe `db.t4g.micro`;
- 20 GiB iniciais, com autoscaling até 100 GiB;
- armazenamento criptografado e sem acesso público;
- senha master gerenciada pelo RDS no Secrets Manager;
- retenção de backup padrão de sete dias;
- instância Single-AZ em todos os ambientes; deletion protection somente em prod;
- final snapshot obrigatório em prod; em dev/QA o código permite ignorá-lo, embora `prevent_destroy` continue bloqueando
  destruição pelo Terraform.

**MongoDB**

O módulo executa a imagem oficial MongoDB `8.0.34` em um serviço ECS Fargate privado com uma tarefa. A API conecta pelo
Cloud Map em `mongo.<project>-<environment>.local:27017`; a porta só aceita tráfego do security group da aplicação.
As credenciais administrativas e da aplicação são geradas pelo Terraform e armazenadas no Secrets Manager. Um container
de inicialização cria/atualiza o usuário da aplicação, limitado a leitura e escrita no database configurado; a API não
recebe a credencial administrativa.

Os dados são gravados em um EFS criptografado, montado por access point com criptografia em trânsito e autorização IAM.
O sistema de arquivos tem proteção contra destruição pelo Terraform e política de backup automático ativada. O serviço
MongoDB não é um banco gerenciado: há apenas uma instância, sem replicação ou failover entre tarefas. Durante atualização,
o ECS para a tarefa anterior antes de iniciar a nova para evitar dois processos MongoDB usando o mesmo diretório de dados.
Antes de produção, teste o comportamento do MongoDB sobre EFS, confirme retenção/restauração dos backups e avalie
disponibilidade, desempenho, sizing e custo.

**Amazon ElastiCache for Redis**

- replication group Redis 7.1 por padrão, classe `cache.t4g.micro` em dev/QA e `cache.t4g.small` em prod;
- um nó em dev/QA e dois nós em prod, sem automatic failover nem Multi-AZ;
- criptografia em repouso e em trânsito, token de autenticação gerado e armazenado no Secrets Manager;
- snapshots com retenção padrão de sete dias.

Redis continua sendo transporte/cache para Redis Streams; o state transacional permanece no PostgreSQL.

O código Terraform não remove automaticamente dados históricos DynamoDB: o recurso antigo é desanexado do state com
`removed { destroy = false }`. A limpeza física desse recurso, se ainda existir na conta, deve ser avaliada e executada
separadamente após revisar os dados e o state de cada ambiente.

### E-mail, parâmetros e segredos

O módulo registra a identidade de e-mail no SES, usando `ses_from_address`, e concede ao task role somente a ação
`ses:SendEmail` para essa identidade. A identidade deve ser verificada na mesma conta e região, e a conta SES pode ainda
estar no sandbox. A validação do Terraform não comprova entrega real.

Parâmetros não secretos vão para Systems Manager Parameter Store: região AWS, remetente SES e issuer/audience/realm do
JWT. Token do Redis e segredo JWT são gerados e armazenados no Secrets Manager; a senha do RDS é
gerenciada pelo próprio RDS. A execution role do ECS recebe permissões para buscar os segredos/parâmetros referenciados
pela task definition. O container recebe essas configurações como variáveis de ambiente no runtime.

O SDK AWS no aplicativo usa as credenciais temporárias do ECS task role para SES; não são configuradas access keys
estáticas no container.

**Ponto pendente para produção:** a task atualmente recebe a credencial master do RDS. Deve-se criar e usar um usuário
PostgreSQL específico para a aplicação, com privilégios mínimos, antes do uso de produção.

### Identidade de deploy GitHub Actions

O módulo cria um IAM user dedicado por ambiente e uma policy inline restrita ao deploy daquele ambiente. A policy
permite autenticar no ECR, publicar imagens no repositório da API, registrar/descrever task definitions,
descrever/atualizar o serviço ECS e passar somente as roles de execução e de task ao ECS.

O workflow `Deploy AWS` recebe as credenciais por GitHub Environments (`dev`, `qa`, `prod`), cada um com os secrets
`AWS_ACCESS_KEY_ID` e `AWS_SECRET_ACCESS_KEY`. Após o primeiro apply, crie uma access key para o username do output
`github_deploy_user_name` no IAM e cadastre os valores nos secrets do Environment correspondente. Terraform não cria
`aws_iam_access_key`: a secret key só é exibida uma vez e armazená-la no state ampliaria o risco de exposição. Essas
credenciais são de longa duração; restrinja o acesso aos Environments, faça rotação periódica e remova chaves antigas.
A organização ainda pode bloquear `iam:CreateUser`, `iam:PutUserPolicy` ou `iam:CreateAccessKey` por SCP; uma policy
IAM permissiva na conta não sobrepõe uma negação explícita da SCP.

## Diferenças entre ambientes

| Propriedade | dev | qa | prod |
|---|---|---|---|
| State S3 | `motordesk/dev/terraform.tfstate` | `motordesk/qa/terraform.tfstate` | `motordesk/prod/terraform.tfstate` |
| Região padrão | `us-east-2` | `us-east-2` | `us-east-2` |
| Zonas de disponibilidade | Duas, selecionadas automaticamente | Duas, selecionadas automaticamente | Duas, selecionadas automaticamente |
| NAT Gateway | Um compartilhado | Um compartilhado | Dois, um por AZ |
| RDS Multi-AZ | Não | Não | Não |
| Redis | Um nó padrão | Um nó padrão | Dois nós, sem Multi-AZ/failover |
| ECS inicial | `desired_count = 0` | `desired_count = 0` | `desired_count = 0` |

O workflow de deploy ajusta ECS para uma tarefa em dev/QA e duas em prod. Custos, quotas, capacidade e parâmetros de
produção precisam de revisão na região e conta escolhidas; os valores no código são defaults iniciais, não sizing
validado de carga.

## Configuração dos roots

Todos os roots configuram:

- `aws_region`, com default `us-east-2`; o módulo escolhe duas AZs disponíveis na região;
- `ses_from_address`, identidade de remetente SES;

QA e prod exigem `api_domain_name`, usado para solicitar o certificado ACM. Em dev, essa variável é opcional; sem ela,
`api_url` usa o DNS do ALB por HTTP. A rede e o ALB cobrem duas AZs; o RDS permanece Single-AZ e o Redis sem failover
automático conforme as configurações atuais.
`route53_zone_id` é opcional. `mongo_database` e `mongo_options` configuram o cliente MongoDB usado pela aplicação. A
credencial do MongoDB é criada pelo módulo e armazenada no Secrets Manager; não é necessário fornecer endpoint ou senha
externos. Variáveis sensíveis não devem ser versionadas; use `.tfvars` local ignorado ou mecanismo seguro equivalente.

O módulo compartilhado oferece ainda customização de CIDRs da VPC e subnets, classes/versões dos bancos, retenção de
backups, alterações imediatas de banco, tamanho ECS, imagem e metadados JWT. Os defaults completos estão em
`modules/application/variables.tf`.

## Automação e comandos

`terraform-aws.main.kts` oferece `fmt`, `validate`, `plan` e `apply`. O script valida o nome do ambiente e não permite
`apply` sem `--confirm-apply true`. `fmt`/`validate` inicializam os providers sem backend; `plan`/`apply` exigem
credenciais AWS e as variáveis `AWS_TF_BACKEND_BUCKET` e `AWS_REGION` (ou `AWS_DEFAULT_REGION`).

Exemplo de bootstrap e configuração do root dev:

```powershell
terraform -chdir=infra/terraform/aws/backend init -backend=false
terraform -chdir=infra/terraform/aws/backend apply `
  -var="state_bucket_name=<nome-globalmente-unico>" `
  -var="aws_region=us-east-2"

$env:AWS_REGION = "us-east-2"
$env:AWS_TF_BACKEND_BUCKET = "<bucket-criado>"
terraform -chdir=infra/terraform/aws/envs/dev init `
  -backend-config="bucket=$env:AWS_TF_BACKEND_BUCKET" `
  -backend-config="region=$env:AWS_REGION"
terraform -chdir=infra/terraform/aws/envs/dev validate
terraform -chdir=infra/terraform/aws/envs/dev plan
```

Também é possível executar a automação Gradle. `validate` não aplica mudanças; `apply` requer confirmação explícita:

```powershell
.\gradlew.bat terraformAws -PawsEnvironment=dev -PawsAction=validate
.\gradlew.bat terraformAws -PawsEnvironment=dev -PawsAction=plan
.\gradlew.bat terraformAws -PawsEnvironment=dev -PawsAction=apply -PconfirmAwsApply=true
```

Revise sempre o plano produzido e confirme a conta, região, environment, recursos públicos, políticas IAM, custos e
efeitos sobre os dados antes de aprovar um apply.

O workflow `Terraform AWS Validation` roda `fmt`, `init -backend=false` e `validate` para dev, QA e prod, sem
credenciais nem provisionamento. O workflow manual `Deploy AWS` executa testes/build, usa as access keys do GitHub
Environment escolhido, envia a imagem ao ECR e atualiza o ECS. Configure a variável de repositório `AWS_REGION` e os
secrets `AWS_ACCESS_KEY_ID` e `AWS_SECRET_ACCESS_KEY` separadamente em cada GitHub Environment (`dev`, `qa`, `prod`).

## Outputs

Os roots expõem URL da API, URL do repositório ECR, nomes de cluster/serviços ECS, endpoint privado do MongoDB, username
do IAM user de deploy e status de verificação da identidade SES. O módulo também define endpoints PostgreSQL e Redis
como outputs.

## Limites e validação antes de produção

- O código Terraform foi formatado e validado localmente, mas não foi aplicado em conta AWS; portanto, não há garantia
  de quotas, disponibilidade regional, custos, políticas efetivas ou entrega SES.
- Nenhuma migração de banco ou de mensagens é feita pelo Terraform ou pelos workflows descritos aqui.
- Testar integração, desempenho, backup/restauração e atualização do MongoDB com o EFS antes de produção, além de
  consumer groups, pending entries e acknowledgements do Redis Streams.
- Verificar certificado ACM, DNS, verificação SES e remoção de restrições de sandbox antes de depender de tráfego real.
- Usar identidade IAM least-privilege para PostgreSQL em produção em vez da credencial master atualmente injetada.
- O apply inicial deve ser revisado com cuidado: estados, senhas geradas e planos Terraform são dados sensíveis.

Consulte também o [runbook AWS](aws-migration-runbook.md) para a sequência operacional de preparação, migração de dados,
cutover, rollback e limpeza de recursos legados.
