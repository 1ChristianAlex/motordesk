# AWS and Kubernetes deployment architecture

This document describes the AWS infrastructure and EKS workloads declared in the repository. It documents configuration,
not resources already provisioned in an AWS account. See the [Mermaid infrastructure diagram](../diagrams/aws-current-infrastructure.md).

## Ownership and state

```text
infra/
├── terraform/aws/
│   ├── backend/                  # One-time S3 state bucket bootstrap
│   ├── envs/{dev,qa,prod}/       # Independent AWS infrastructure states
│   └── modules/application/      # VPC, EKS, data services, IAM and ACM
└── kubernetes/
    ├── base/                     # Kubernetes YAML for workloads and ingress
    ├── helm/                     # Pinned Helm versions and add-on installer
    └── deploy-workloads.sh       # Applies manifests and verifies health
```

Terraform owns the AWS foundation and AWS integrations only. Kubernetes YAML owns namespace, ConfigMaps, Deployments,
Services, Ingress, HPA, PDB, SecretStores, ExternalSecrets, MongoDB StatefulSet and persistent-volume configuration.
Helm installs the AWS Load Balancer Controller, External Secrets Operator, Metrics Server, Cluster Autoscaler and,
optionally, ExternalDNS. The AWS Terraform state remains `motordesk/<environment>/terraform.tfstate`; Kubernetes objects
are managed by the cluster API, not separate Terraform states.

The EKS API endpoint is private, so deployment requires the VPC-connected `eks-private` Linux runner.

## AWS foundation

Each environment owns a VPC with public, private application and private data subnets. Terraform provisions:

- EKS control plane, private endpoint, KMS encryption and control-plane audit logs;
- managed node group with Terraform-owned scaling bounds and Cluster Autoscaler IAM permissions/tags;
- VPC CNI, CoreDNS, kube-proxy, Pod Identity Agent, EFS CSI and CloudWatch Observability EKS add-ons;
- ECR with immutable tags, push scanning and lifecycle retention;
- RDS PostgreSQL, ElastiCache Redis, encrypted EFS, `/mongo` access point and backup policy;
- IAM Pod Identity roles for SES, External Secrets, EFS CSI, AWS Load Balancer Controller, Cluster Autoscaler and
  optional ExternalDNS;
- Secrets Manager, Parameter Store, ACM certificate and optional Route 53 DNS validation records.

Public subnets are tagged for AWS Load Balancer Controller discovery. The controller creates the ALB from the API Ingress
and manages its target groups/security-group rules. ExternalDNS manages the custom-domain alias only when a Route 53 zone
is configured. If no hosted zone is configured, DNS records must be created externally.

Review current EKS, EC2, ALB, NAT, EFS, database and CloudWatch costs and regional quotas before apply. Node sizes and
database defaults are not load-tested recommendations.

## Kubernetes workloads and secrets

`infra/kubernetes/base/` contains the API Deployment and ClusterIP Service, environment-specific CPU HPA, production PDB,
ALB Ingress, MongoDB StatefulSet/Services, static EFS PV template, ConfigMap, ServiceAccount, SecretStores and
ExternalSecrets. The deploy script renders only runtime identifiers from Terraform outputs; no secret values are placed in
the repository or workflow logs.

External Secrets Operator reads Secrets Manager and Parameter Store via EKS Pod Identity. The API uses a separate Pod
Identity role for SES. AWS Load Balancer Controller and ExternalDNS use service-account-scoped IAM roles through EKS
Pod Identity.
Metrics Server provides resource metrics for HPA.

MongoDB mounts the existing EFS access point through a static CSI PV with reclaim policy `Retain`. EFS and its access point
have Terraform `prevent_destroy`; EFS persistence is not MongoDB replication or a backup replacement.

## Migration behavior

The EKS-only AWS plan removes legacy ECS, API Gateway and Terraform-managed NLB resources. The API is unavailable until the
Helm add-ons and YAML workloads are applied and the Ingress ALB becomes healthy. Before confirming apply, inspect the plan,
verify the EFS recovery point and restore rehearsal, and ensure the previous MongoDB writer has stopped.

## Deploy and validate

`Deploy EKS Runtime` runs manually from a private Linux runner. It always plans AWS Terraform; applying requires explicit
confirmation of the reviewed plan and EFS maintenance window. On apply, it builds/pushes an immutable API image, configures
private EKS access, installs Helm add-ons, applies Kubernetes YAML and verifies MongoDB/API rollouts and `/health`.

`Terraform AWS Validation` runs formatting, initialization and validation for the AWS roots and checks Kubernetes YAML and
deployment-script syntax. It does not apply changes. See the [AWS runbook](aws-migration-runbook.md) for credentials,
migration steps, persistent-data safety and recovery.
