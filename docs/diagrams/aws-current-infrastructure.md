# Infraestrutura AWS do Motor Desk

Diagrama da arquitetura EKS-only declarada pelo Terraform e pelos manifests/Helm em `infra/kubernetes/`. Representa a
configuração desejada no repositório, não confirma que os recursos já foram provisionados na conta AWS.

```mermaid
flowchart TB
    OP["Operador"]
    GH["GitHub Actions<br/>Deploy EKS Runtime"]
    RUNNER["Runner Linux privado<br/>VPC / eks-private"]
    TF["Terraform<br/>infra/terraform/aws"]
    K8S["Kubernetes YAML<br/>infra/kubernetes/base"]
    HELM["Helm<br/>infra/kubernetes/helm"]
    STATE[("S3 Terraform state")]
    ECR[("Amazon ECR<br/>tag imutável")]

    subgraph VPC["VPC independente por ambiente"]
        subgraph PUBLIC["Subnets públicas"]
            IGW["Internet Gateway"]
            NAT["NAT Gateway"]
            ALB["ALB internet-facing<br/>AWS Load Balancer Controller"]
        end

        subgraph APP["Subnets privadas de aplicação"]
            subgraph EKS["Amazon EKS<br/>endpoint público e privado"]
                CONTROL["Control plane"]
                NODES["Managed node group<br/>capacidade via Terraform"]
                subgraph NS["Namespace motordesk"]
                    API["API Deployment<br/>HPA + probes"]
                    APISVC["API ClusterIP Service"]
                    ING["Ingress"]
                    MS["MongoDB StatefulSet<br/>1 réplica"]
                    MSVC["MongoDB ClusterIP"]
                    PVC["PVC + PV estático<br/>Retain"]
                    ES["ExternalSecrets"]
                end
                LBC["AWS Load Balancer Controller"]
                ESO["External Secrets Operator"]
                METRICS["Metrics Server"]
                CA["Cluster Autoscaler"]
                NRAGENT["New Relic Kubernetes integration<br/>namespace newrelic"]
                CWAGENT["CloudWatch Observability<br/>EKS add-on"]
            end
        end

        subgraph DATA["Subnets privadas de dados"]
            PG[("RDS PostgreSQL")]
            REDIS[("ElastiCache Redis<br/>TLS + token")]
            EFS[("EFS criptografado<br/>backup habilitado")]
            AP["EFS access point<br/>/mongo, UID/GID 999"]
        end
    end

    SM[("Secrets Manager")]
    SSM[("Systems Manager<br/>Parameter Store")]
    SES["Amazon SES"]
    DNS["Route 53<br/>ExternalDNS opcional"]
    ACM["ACM certificate"]
    NR[("New Relic<br/>métricas, eventos e OTLP")]
    CW[("CloudWatch Logs<br/>logs de workloads e control plane")]

    OP --> GH
    GH --> RUNNER
    RUNNER --> TF
    RUNNER --> K8S
    RUNNER --> HELM
    TF --> STATE
    RUNNER -->|"build / push"| ECR
    ECR -->|"pull"| API

    ING --> ALB --> APISVC --> API
    API --> PG
    API --> REDIS
    API --> MSVC --> MS
    MS --> PVC --> AP --> EFS

    SM --> ESO
    SSM --> ESO
    ESO --> ES
    ES --> API
    ES --> MS
    API -->|"Pod Identity"| SES
    API -->|"OpenTelemetry OTLP"| NR
    LBC -->|"Pod Identity"| ALB
    DNS -.->|"Ingress alias"| ALB
    ACM --> ALB

    CONTROL --> NODES
    NODES --> METRICS
    HELM --> LBC
    HELM --> ESO
    HELM --> METRICS
    HELM --> CA
    HELM --> NRAGENT
    GH -->|"sincroniza Secret"| NRAGENT
    HELM -.-> DNS
    CA -->|"ASG bounds from Terraform"| NODES
    NRAGENT -->|"métricas e eventos"| NR
    NODES --> CWAGENT --> CW
    CONTROL -->|"audit/control plane logs"| CW

    APP --> NAT
    NAT --> IGW
    IGW --> INTERNET["Internet"]
```

## Operação e persistência

- Terraform provisiona VPC, subnets, Security Groups, EKS, Node Groups, IAM/Pod Identity, RDS, ElastiCache, EFS, ECR e ACM.
- YAML declara Deployments, Services, Ingress, HPA, PDB, ConfigMaps, SecretStores/ExternalSecrets e StatefulSet.
- Helm instala AWS Load Balancer Controller, External Secrets Operator, Metrics Server, Cluster Autoscaler, New Relic
  Kubernetes observability e, com Route 53 configurado, ExternalDNS.
- CloudWatch permanece como destino dos logs de workloads e control plane; New Relic coleta métricas/eventos Kubernetes e
  recebe a telemetria OpenTelemetry já exportada pela API.
- MongoDB reutiliza o EFS/access point existente; PV usa `Retain` e os recursos EFS têm `prevent_destroy`.
- ALB e endpoint de domínio substituem API Gateway/NLB para o tráfego da aplicação. Verifique a janela de manutenção na
  [ADR-008](../ADR/ADR-008-Amazon-EKS-Runtime.md) e no [runbook](../deployment/aws-migration-runbook.md).
