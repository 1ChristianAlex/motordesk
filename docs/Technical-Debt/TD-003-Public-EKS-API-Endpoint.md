# TD-003 - Public EKS API Endpoint Open to All IPv4 Addresses

## Status

Open — temporary configuration for study use

## Priority

High before production

## Context

The EKS cluster currently enables its public Kubernetes API endpoint and allows connections from `0.0.0.0/0`. This
was configured so GitHub-hosted Actions runners, whose outbound IP can vary, can run `kubectl` and deploy workloads
without a self-hosted runner or fixed egress IP.

The cluster also enables its private endpoint. Kubernetes API access still requires AWS IAM authentication and the
permissions associated with the caller's EKS access entry; public network reachability does not by itself grant cluster
access.

## Risk

Allowing all IPv4 addresses to reach the control-plane endpoint exposes the Kubernetes API to the public internet. This
increases the attack surface for scanning, denial-of-service attempts, and exploitation of future authentication,
authorization, or control-plane vulnerabilities. The current restriction to authenticated and authorized IAM principals
reduces but does not eliminate this exposure.

This configuration is for educational exploration only and is not an acceptable production network boundary.

## Temporary Decision

Keep the public endpoint enabled with `public_access_cidrs = ["0.0.0.0/0"]` while using GitHub-hosted runners for study
deployments. Do not treat this as the long-term security configuration.

## Proposed Remediation

Use one of the following approaches before production:

1. Restrict `public_access_cidrs` to stable, trusted egress CIDRs and keep the public endpoint enabled.
2. Disable public endpoint access and run deployments from a runner or network connected to the VPC.

In either case, retain least-privilege EKS access policies and review the Terraform plan to confirm the endpoint settings
before applying changes.

## Acceptance Criteria

- [ ] The public endpoint is disabled or restricted to explicitly approved, stable CIDRs.
- [ ] The deployment runner can reach the EKS API using the selected network design.
- [ ] EKS IAM access is limited to the required deployment identity and permissions.
- [ ] Terraform validation and a reviewed plan confirm the intended endpoint configuration.
