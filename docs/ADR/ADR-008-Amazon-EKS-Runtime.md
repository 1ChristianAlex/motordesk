# ADR-008 — Amazon EKS with Kubernetes YAML and Helm

- **Status:** Accepted — implementation is in progress; AWS provisioning and migration have not been run.
- **Decision date:** 2026-11-01

## Context

Motor Desk uses AWS-managed PostgreSQL, Redis, EFS, ECR, Secrets Manager, Parameter Store and SES. The application runtime
is moving to EKS, and the existing MongoDB data on EFS must remain available. Infrastructure ownership should be split
between Terraform for AWS resources and Kubernetes/Helm for cluster workloads.

## Decision

- EKS is the only application runtime. Do not provision ECS services, task definitions or clusters.
- Terraform under `infra/terraform/aws/` provisions the VPC, subnets, security groups, EKS cluster and node groups, AWS
  databases/services, ECR, IAM, OIDC provider, EFS, ACM and Route 53 validation records.
- `infra/kubernetes/base/` contains Kubernetes YAML for namespace, Deployments, Services, ConfigMaps, SecretStores,
  ExternalSecrets, Ingress, HPA, PDB and MongoDB storage/workloads.
- Helm installs AWS Load Balancer Controller, External Secrets Operator, Metrics Server and Cluster Autoscaler. ExternalDNS
  is installed when a Route 53 hosted zone is configured.
- The AWS Load Balancer Controller creates an internet-facing ALB from the API Ingress. Custom API domains and Route 53
  hosted zones are optional; when both are configured, Terraform validates the ACM certificate and ExternalDNS manages
  the API record. Otherwise, DNS and certificate validation can be managed externally.
- Terraform creates the EKS OIDC provider and scoped IAM roles for AWS Load Balancer Controller and ExternalDNS.
  EKS Pod Identity remains in use for the API's SES access, External Secrets and EFS CSI.
- Secrets remain in Secrets Manager and Parameter Store. External Secrets Operator synchronizes them into Kubernetes
  Secrets; committed manifests contain references, not secret values.
- MongoDB remains a single-replica StatefulSet on the existing EFS filesystem/access point, through a static PV with
  reclaim policy `Retain`. Terraform protects the filesystem and access point from destruction.
- Managed node-group sizes and Cluster Autoscaler bounds remain provisioned/configured through Terraform. HPA, production
  PDB, Deployments and Services are Kubernetes YAML.

## Migration and operations

The first EKS-only apply removes legacy ECS, API Gateway and Terraform-managed NLB resources. The API remains unavailable
until the ALB Ingress and Kubernetes workloads are healthy. Treat this as a maintenance migration: review the AWS plan,
confirm an EFS recovery point and restore rehearsal, and ensure the old MongoDB writer has stopped before the StatefulSet
starts. Never allow two MongoDB processes to write to the same EFS directory.

Deployments run from a VPC-connected runner because the EKS API endpoint is private. The workflow applies AWS Terraform,
installs pinned Helm charts, then applies Kubernetes YAML. Review the AWS plan before confirming apply.

## Alternatives considered

- **Keep ECS:** does not meet the EKS-only runtime requirement.
- **Manage Kubernetes resources through Terraform:** rejected to keep the AWS resource lifecycle separate from Kubernetes
  workload deployment, as requested.
- **Use API Gateway and a Terraform-managed NLB:** rejected because the API workload should be exposed through a
  Kubernetes Ingress reconciled by AWS Load Balancer Controller.
- **EKS Auto Mode:** reduces node operations but changes the compute management model; use Terraform-managed node groups
  for the initial implementation.

## Consequences

- The ALB, EKS control plane, EC2 nodes, NAT, EFS and CloudWatch incur ongoing costs; review the target region and
  environment sizing before provisioning.
- ExternalDNS needs Route 53 permissions scoped to the configured hosted zone. The load balancer controller requires
  AWS permissions to reconcile load balancers and security-group rules; its IAM policy is pinned to the chart release.
- MongoDB remains self-managed with one replica; EFS persistence does not provide database replication or high availability.
- Replacing API Gateway with an Ingress changes the public endpoint and removes API Gateway-specific features/logging.
- The AWS and Kubernetes deployment steps are ordered dependencies. A failed migration can leave the API unavailable until
  the old or new runtime is restored.

## References

- [Amazon EKS managed node groups](https://docs.aws.amazon.com/eks/latest/userguide/managed-node-groups.html)
- [AWS Load Balancer Controller](https://docs.aws.amazon.com/eks/latest/userguide/aws-load-balancer-controller.html)
- [Amazon EKS EFS CSI driver](https://docs.aws.amazon.com/eks/latest/userguide/efs-csi.html)
- [AWS Terraform architecture](../deployment/aws-terraform-architecture.md)
- [AWS operations runbook](../deployment/aws-migration-runbook.md)
