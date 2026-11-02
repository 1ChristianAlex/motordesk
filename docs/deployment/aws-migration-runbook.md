# AWS deployment and operations runbook

This runbook covers the EKS-only runtime. Terraform provisions AWS infrastructure; Kubernetes YAML and Helm manage EKS
workloads and add-ons. It does not execute AWS operations, migrate database contents, or restore backups by itself.

## Ownership and prerequisites

- `infra/terraform/aws/envs/{dev,qa,prod}` provisions the VPC, EKS and node group, ECR, EFS, databases, IAM, secrets,
  ACM and related AWS services.
- `infra/kubernetes/base` contains Kubernetes YAML for API, MongoDB, ingress, scaling, ConfigMaps and External Secrets.
- `infra/kubernetes/helm` pins and installs AWS Load Balancer Controller, External Secrets Operator, Metrics Server,
  Cluster Autoscaler and optional ExternalDNS.
- The workflow uses a GitHub-hosted `ubuntu-latest` runner with AWS CLI, Docker, Terraform, kubectl, Helm, envsubst and
  curl. The EKS API endpoint is public and permits all IPv4 addresses so the runner can reach it without a fixed egress
  IP. IAM authentication and EKS authorization are still required. This unrestricted network access is for study use;
  restrict the endpoint CIDRs before production.

Before deploying, confirm the AWS account/region, environment, state bucket, GitHub Environment protections, quotas and
budget. Configure `AWS_REGION`, `AWS_TF_BACKEND_BUCKET`, `API_DOMAIN_NAME`, `ROUTE53_ZONE_ID` and `SES_FROM_ADDRESS` as
GitHub Environment variables. Provisioning and EKS deploy credentials are separate; keep them protected and do not store
credentials in repository variables or source control.

## First EKS-only migration

1. Verify a recent EFS recovery point and a completed restore rehearsal. Confirm the current MongoDB data on EFS is
   authoritative and that no second writer will use the same directory.
2. Dispatch `Deploy EKS Runtime` for the target environment with `confirm_apply=false` to inspect the AWS plan.
3. Confirm the plan removes legacy ECS, API Gateway and Terraform-managed NLB resources, but does not replace or destroy
   the EFS filesystem/access point, RDS, Redis or unrelated data.
4. Approve a maintenance window and dispatch again with both `confirm_apply=true` and `confirm_data_cutover=true`.
5. The workflow applies AWS Terraform, installs pinned Helm add-ons, applies Kubernetes YAML, and checks workload and API
   health. The API is unavailable between removing API Gateway and the Ingress ALB becoming healthy.

The EKS deploy IAM user has ECR push/describe and EKS access. Its EKS access entry currently grants cluster-admin; protect
the credentials and restrict that scope as a follow-up hardening measure.

## API ingress and DNS

The AWS Load Balancer Controller reconciles `infra/kubernetes/base/ingress.yaml` into an internet-facing ALB. When
`api_domain_name` and `route53_zone_id` are both optional in every environment. Without a custom domain, use the ALB
hostname. When both values are configured, Terraform creates the ACM DNS validation record and ExternalDNS manages the
Ingress alias. If a custom domain is used without a hosted zone, manage ACM validation and the ALB alias externally.

ALB ingress requires the public subnet discovery tags created by Terraform. The controller is configured with region and
VPC ID explicitly rather than relying on pod access to EC2 instance metadata.

## MongoDB data safety

The MongoDB StatefulSet mounts the existing EFS access point at `/mongo` into `/data/db`. The static CSI PV uses reclaim
policy `Retain`; Terraform protects the filesystem and access point with `prevent_destroy`. Never run two MongoDB writers
against the same data directory.

Before migration or recovery, stop/isolate all MongoDB writers, verify the recovery point, wait for the MongoDB pod to stop
before restoring or mounting the filesystem elsewhere, and reconcile writes accepted since the recovery point. EFS is
persistent storage, not database replication or a backup replacement. MongoDB remains a single replica.

## Regular deployment and rollback

Dispatch `Deploy EKS Runtime` with `confirm_apply=true` only after reviewing the printed AWS plan. The workflow builds and
pushes the commit-SHA image, installs Helm add-ons, applies Kubernetes manifests and checks API/MongoDB health. For a
failed application release, deploy the previous known-good immutable image tag. Do not scale MongoDB beyond one replica or
delete the PV/PVC as an application rollback.

If MongoDB will not start, stop API writes, preserve the EFS filesystem/access point, inspect pod events and EFS CSI logs,
and follow the tested restore/reconciliation procedure. If deployment fails between AWS and Kubernetes steps, inspect the
AWS plan/state and cluster before retrying.

## Post-deployment checks

- API `/health`, authentication and representative Service Order operations;
- PostgreSQL reads/writes and connection limits;
- MongoDB history reads/writes and persistence across pod replacement;
- Redis Streams and SES delivery;
- ExternalSecret readiness and refresh;
- API HPA, Cluster Autoscaler bounds, ALB target health and CloudWatch logs.

PostgreSQL remains Single-AZ, Redis automatic failover remains disabled, and the application currently receives the RDS
master username/password. Replace it with a least-privilege application role before production use.
