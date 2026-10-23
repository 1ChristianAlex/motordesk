# Arquitetura Terraform na AWS

Este documento descreve os recursos AWS declarados em `infra/terraform/aws/`, como os ambientes são separados e como
Terraform se integra ao runtime e aos workflows do Motor Desk. Ele documenta a configuração versionada; **não afirma que
os recursos foram criados em uma conta AWS**. A infraestrutura não foi aplicada e ainda depende de configuração e
validação operacional.

## Visão geral

O Terraform mantém uma implementação compartilhada e três roots independentes:

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

Cada root fixa o ambiente e sua chave própria de state no S3 e chama `modules/application`. Os providers estão fixados
em AWS `6.0.0` e Random `3.7.2`; os roots exigem Terraform `>= 1.10.0`. A região padrão é `sa-east-1`.

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

O módulo cria uma VPC (CIDR padrão `10.40.0.0/16`) distribuída pelas duas primeiras zonas de disponibilidade
disponíveis na região. Em cada AZ são criadas:

- uma subnet pública para o Application Load Balancer e NAT;
- uma subnet privada de aplicação para as tarefas ECS;
- uma subnet privada de dados para RDS, DocumentDB e ElastiCache.

Um Internet Gateway atende as subnets públicas. As subnets de aplicação enviam tráfego de saída por NAT Gateway; dev e
QA compartilham um NAT Gateway, enquanto produção cria um NAT Gateway por AZ. As subnets de dados não têm rota default
para a internet.

O ALB é público. Seu security group aceita HTTP/80 e HTTPS/443 da internet, e permite saída para a porta 8080 dentro da
VPC. O listener HTTP redireciona para HTTPS; o listener HTTPS encaminha para o target group da API e exige um ARN de
certificado ACM. A política TLS configurada é `ELBSecurityPolicy-TLS13-1-2-2021-06`.

O target group usa targets do tipo IP e verifica `GET /health` a cada 30 segundos, esperando status 200. Se
`route53_zone_id` for preenchido, o módulo cria um registro A alias apontando para o ALB; caso contrário, o DNS deve ser
configurado fora do Terraform.

As tarefas ECS não recebem IP público. O security group da aplicação aceita a porta 8080 somente do ALB e libera saída
para tráfego externo através do NAT. Os security groups de PostgreSQL (5432), DocumentDB (27017) e Redis (6379) aceitam
conexões somente do security group das tarefas.

### Runtime, imagem e observabilidade

O módulo cria:

- um repositório ECR por ambiente, com tags imutáveis, varredura de imagem no push e criptografia AES-256;
- uma política de ciclo de vida que retém as duas imagens mais recentes;
- um cluster ECS com Container Insights;
- uma task definition e um serviço ECS Fargate usando `awsvpc`, CPU padrão de 512 unidades e memória de 1024 MiB;
- um grupo de logs CloudWatch com retenção de 30 dias.

O serviço inicia com `desired_count = 0` por padrão, até existir uma imagem inicial no ECR. O workflow de deploy publica a
imagem, registra uma nova revisão da task definition e configura o serviço para uma tarefa em dev/QA ou duas em prod.
O recurso ECS ignora mudanças Terraform em `task_definition` e `desired_count` para que um apply posterior não reverta a
versão ou escala definida pelo deploy.

### Persistência

**Amazon RDS for PostgreSQL**

- PostgreSQL 16.4 por padrão, classe `db.t4g.micro`;
- 20 GiB iniciais, com autoscaling até 100 GiB;
- armazenamento criptografado e sem acesso público;
- senha master gerenciada pelo RDS no Secrets Manager;
- retenção de backup padrão de sete dias;
- Multi-AZ e deletion protection somente em prod;
- final snapshot obrigatório em prod; em dev/QA o código permite ignorá-lo, embora `prevent_destroy` continue bloqueando
  destruição pelo Terraform.

**Amazon DocumentDB**

- cluster compatível com MongoDB, engine 5.0.0 por padrão, em subnets de dados;
- instância padrão `db.t3.medium`; uma instância em dev/QA por padrão e duas em prod;
- armazenamento criptografado, TLS habilitado e backups com a retenção configurada;
- credencial gerada pelo provider Random, guardada no Secrets Manager e injetada no container.

DocumentDB é uma implementação candidata, não uma compatibilidade MongoDB presumida. Em QA e prod, a variável
`documentdb_compatibility_confirmed` precisa ser `true`; essa validação apenas exige uma declaração do operador. Antes de
confirmá-la, é necessário executar testes contra a engine escolhida que cubram queries, índices, agregações, TLS e
operações de histórico realmente usadas pela aplicação.

**Amazon ElastiCache for Redis**

- replication group Redis 7.1 por padrão, classe `cache.t4g.micro` em dev/QA e `cache.t4g.small` em prod;
- um nó em dev/QA e dois nós em prod, com automatic failover e Multi-AZ em prod;
- criptografia em repouso e em trânsito, token de autenticação gerado e armazenado no Secrets Manager;
- snapshots com retenção padrão de sete dias.

Redis continua sendo transporte/cache para Redis Streams; o state transacional permanece no PostgreSQL.

RDS, DocumentDB e ElastiCache têm `prevent_destroy` para evitar remoção acidental por um plano Terraform.

### E-mail, parâmetros e segredos

O módulo registra a identidade de e-mail no SES, usando `ses_from_address`, e concede ao task role somente a ação
`ses:SendEmail` para essa identidade. A identidade deve ser verificada na mesma conta e região, e a conta SES pode ainda
estar no sandbox. A validação do Terraform não comprova entrega real.

Parâmetros não secretos vão para Systems Manager Parameter Store: região AWS, remetente SES e issuer/audience/realm do
JWT. Senha do DocumentDB, token do Redis e segredo JWT são gerados e armazenados no Secrets Manager; a senha do RDS é
gerenciada pelo próprio RDS. A execution role do ECS recebe permissões para buscar os segredos/parâmetros referenciados
pela task definition. O container recebe essas configurações como variáveis de ambiente no runtime.

O SDK AWS no aplicativo usa as credenciais temporárias do ECS task role para SES; não são configuradas access keys
estáticas no container.

**Ponto pendente para produção:** a task atualmente recebe a credencial master do RDS. Deve-se criar e usar um usuário
PostgreSQL específico para a aplicação, com privilégios mínimos, antes do uso de produção.

### Identidade de deploy GitHub Actions

O módulo configura um provedor OIDC para `token.actions.githubusercontent.com` e uma role de deploy por ambiente. A
trust policy limita `aud` a `sts.amazonaws.com` e exige igualdade exata do claim `sub` com `github_oidc_subject`.

A policy permite obter token de autenticação e publicar imagens no repositório ECR da API, registrar/descrever task
definitions, descrever/atualizar o serviço ECS e passar somente as roles de execução e de task ao ECS. O subject padrão
do módulo é o branch `dev`; QA/prod exigem configurar o subject correspondente de forma explícita. Se a conta já possuir
o provedor OIDC, sua existência deve ser conferida antes de um apply para evitar tentativa de duplicá-lo.

## Diferenças entre ambientes

| Propriedade | dev | qa | prod |
|---|---|---|---|
| State S3 | `motordesk/dev/terraform.tfstate` | `motordesk/qa/terraform.tfstate` | `motordesk/prod/terraform.tfstate` |
| Região padrão | `sa-east-1` | `sa-east-1` | `sa-east-1` |
| NAT Gateway | Um compartilhado | Um compartilhado | Um por AZ |
| RDS Multi-AZ | Não | Não | Sim |
| DocumentDB | Uma instância padrão | Uma instância padrão | Duas instâncias |
| Redis | Um nó padrão | Um nó padrão | Dois nós, Multi-AZ/failover |
| Proteções de exclusão RDS/DocumentDB | `prevent_destroy` | `prevent_destroy` | `prevent_destroy` e deletion protection |
| ECS inicial | `desired_count = 0` | `desired_count = 0` | `desired_count = 0` |
| Confirmação DocumentDB | Não exigida pelo root | Obrigatória | Obrigatória |

O workflow de deploy ajusta ECS para uma tarefa em dev/QA e duas em prod. Custos, quotas, capacidade e parâmetros de
produção precisam de revisão na região e conta escolhidas; os valores no código são defaults iniciais, não sizing
validado de carga.

## Configuração dos roots

Todos os roots configuram:

- `aws_region`, com default `sa-east-1`;
- `certificate_arn`, cujo certificado ACM cobre o hostname da API;
- `ses_from_address`, identidade de remetente SES;
- `api_domain_name`;
- `github_oidc_subject`, subject OIDC permitido para deploy. O root dev tem um default para o branch `dev`; QA e prod
  exigem valor explícito.

`route53_zone_id` é opcional. QA e prod também exigem
`documentdb_compatibility_confirmed=true` após os testes de compatibilidade. Variáveis sensíveis não devem ser
versionadas; use `.tfvars` local ignorado ou mecanismo seguro equivalente.

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
  -var="aws_region=sa-east-1"

$env:AWS_REGION = "sa-east-1"
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
credenciais nem provisionamento. O workflow manual `Deploy AWS` executa testes/build, autentica via OIDC, envia a
imagem ao ECR e atualiza o ECS. Ele pressupõe a configuração das variáveis GitHub:
`AWS_REGION`, `AWS_DEV_DEPLOY_ROLE_ARN`, `AWS_QA_DEPLOY_ROLE_ARN` e `AWS_PROD_DEPLOY_ROLE_ARN`.

## Outputs

Os roots expõem URL HTTPS da API, URL do repositório ECR, nome de cluster/serviço ECS, ARN da role de deploy e status de
verificação da identidade SES. O módulo também define endpoints PostgreSQL, DocumentDB e Redis como outputs; os roots
de ambiente atualmente não os reexportam.

## Limites e validação antes de produção

- O código Terraform foi formatado e validado localmente, mas não foi aplicado em conta AWS; portanto, não há garantia
  de quotas, disponibilidade regional, custos, políticas efetivas ou entrega SES.
- Nenhuma migração de banco ou de mensagens é feita pelo Terraform ou pelos workflows descritos aqui.
- Testar restauração de backups e compatibilidade DocumentDB, além de consumer groups, pending entries e acknowledgements
  do Redis Streams.
- Verificar certificado ACM, DNS, verificação SES e remoção de restrições de sandbox antes de depender de tráfego real.
- Usar identidade IAM least-privilege para PostgreSQL em produção em vez da credencial master atualmente injetada.
- O apply inicial deve ser revisado com cuidado: estados, senhas geradas e planos Terraform são dados sensíveis.

Consulte também o [runbook AWS](aws-migration-runbook.md) para a sequência operacional de preparação, migração de dados,
cutover, rollback e limpeza de recursos legados.
