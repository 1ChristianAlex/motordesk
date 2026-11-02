# TD-004 — OIDC Blocked by AWS Organizations Service Control Policy

## Status

Open — EKS workloads currently use EKS Pod Identity; GitHub Actions continues to use IAM access keys.

## Priority

Medium for the current study deployment; high before production or removing long-lived GitHub credentials.

## Context

The EKS Terraform apply failed while creating an IAM OIDC provider for the cluster:

```text
iam:CreateOpenIDConnectProvider
AccessDenied with an explicit deny in an AWS Organizations service control policy
```

The denied resource was the EKS cluster's OIDC issuer. This provider was intended for Kubernetes workloads such as the
AWS Load Balancer Controller and optional ExternalDNS to assume scoped IAM roles through IAM Roles for Service Accounts
(IRSA).

This is distinct from GitHub Actions authentication. Before the migration from ECS to EKS, the ECS infrastructure did
not create an IAM OIDC provider. The GitHub Actions workflow already authenticated to AWS with IAM access keys stored
as GitHub secrets, and it continues to use those credentials. The failed request was Terraform creating an OIDC
provider for EKS pods, not GitHub Actions attempting to authenticate through OIDC.

The explicit deny is imposed by an AWS Organizations SCP. Adding an IAM allow policy or using an administrator-level
principal in the member account cannot override that deny; the organization policy must be changed by an authorized
organization administrator.

## Problem

The member account cannot create the IAM OIDC provider required by the original EKS IRSA configuration. This blocks
that workload-identity approach regardless of the account user's IAM administrator permissions.

GitHub Actions also continues to rely on long-lived access keys. Those keys must be protected as secrets, restricted to
the minimum deployment permissions, and rotated. Moving the workflow to GitHub OIDC would be a separate change and
would require an IAM OIDC provider for GitHub Actions; whether the SCP permits that provider has not been verified.

## Current Workaround

Use EKS Pod Identity for EKS workloads that need AWS permissions. The Terraform configuration uses Pod Identity
associations for the AWS Load Balancer Controller and optional ExternalDNS, avoiding creation of the EKS IAM OIDC
provider. Other existing EKS workloads also use Pod Identity.

Continue using the dedicated deployment IAM user's access keys for GitHub Actions, stored as GitHub Environment
secrets. Do not use root or administrator credentials for the workflow.

## Proposed Remediation

1. Keep EKS Pod Identity for supported Kubernetes workloads while the organization SCP denies OIDC provider creation.
2. Ask the AWS Organizations administrator to review whether a narrowly scoped exception for
   `iam:CreateOpenIDConnectProvider` is acceptable. If approved, evaluate GitHub Actions OIDC separately and scope its
   role trust policy to the repository and deployment environment.
3. Until GitHub OIDC is permitted and implemented, keep deployment credentials in a dedicated, least-privilege IAM
   identity; limit access to the GitHub Environments that can deploy and rotate the access keys.
4. Do not attempt to bypass the SCP with broader member-account IAM permissions.

## Acceptance Criteria

- [ ] EKS workloads that need AWS permissions use scoped EKS Pod Identity associations unless the organization policy
      changes.
- [ ] GitHub Actions uses a dedicated deployment identity with least-privilege permissions, not root or administrator
      access keys.
- [ ] Access to deployment secrets is restricted to the appropriate GitHub Environments and the keys are rotated.
- [ ] Any future GitHub OIDC migration is validated with the organization administrator and does not assume the SCP
      permits creation of the GitHub OIDC provider.
- [ ] Terraform plan and deployment documentation match the selected identity mechanism.

## Related Documentation

- `docs/ADR/ADR-007-Migrate-Infrastructure-from-Microsoft-Azure-to-AWS.md`
- `docs/ADR/ADR-008-Amazon-EKS-Runtime.md`
- `docs/deployment/aws-terraform-architecture.md`
- `.github/workflows/deploy-eks.yml`
