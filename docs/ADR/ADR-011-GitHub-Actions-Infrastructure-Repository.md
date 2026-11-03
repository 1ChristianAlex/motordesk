# ADR-011 — Separating Application and Infrastructure Repositories

- **Status:** Accepted — repository separation and reusable workflows are implemented; end-to-end deployment validation is pending.
- **Decision date:** 2026-11-03

## Context

Motor Desk infrastructure has a lifecycle and review surface distinct from the Kotlin application. Terraform definitions,
Kubernetes manifests, Helm installers, and infrastructure validation scripts are maintained in
`1ChristianAlex/motordesk-infrastructure`; application source and its Docker build context remain in
`1ChristianAlex/motordesk`.

The application still needs CI tests and an operator-initiated EKS deployment. Duplicating full workflow implementations
in both repositories would allow deployment behavior and infrastructure validation to drift. Reusable GitHub Actions
workflows provide a shared implementation, but the caller and called workflow have separate checkouts and concurrency
contexts that must be handled explicitly.

## Decision

- Keep application source, Gradle build, and the production Docker build context in the application repository.
- Keep Terraform, Kubernetes YAML, Helm installers, and infrastructure validation scripts in the infrastructure
  repository.
- Define reusable CI, deployment, and infrastructure-validation workflows in the infrastructure repository using
  `workflow_call`. Callers pass typed inputs and explicitly map the required secrets.
- Keep user-facing workflow triggers in the application repository. The deployment remains manually initiated with
  `workflow_dispatch`, including an environment choice and explicit apply/data-cutover confirmations.
- In the reusable deployment workflow, check out the caller repository into the default workspace and check out the
  infrastructure repository into the `infrastructure/` subdirectory. Build the image from the application workspace;
  run Terraform and Kubernetes deployment files from the infrastructure checkout.
- Run Terraform and Kubernetes validation directly on pull requests and pushes to the infrastructure repository. Keep
  `workflow_call` available for other repositories that need to invoke the same validation.
- Serialize deployments by environment with the `eks-deploy-${{ inputs.environment }}` concurrency group in the reusable
  deployment workflow only. Do not define the same group in the caller workflow: GitHub Actions can deadlock when a
  top-level workflow and its called workflow contend for the same concurrency group. Keep `cancel-in-progress: false`
  so a deployment already holding the environment lock is not cancelled by a newer run.
- Grant only the permissions needed by the workflows and pass deployment credentials and provider secrets explicitly;
  do not rely on implicit secret inheritance.

## Alternatives considered

### Keep all infrastructure files in the application repository

This avoids cross-repository checkout, but couples infrastructure changes and access control to the application
repository and works against the decision to manage infrastructure separately.

### Duplicate workflows in both repositories

This makes each repository self-contained, but duplicates maintenance and risks inconsistent CI, validation, and
deployment behavior.

### Put the deployment lock only in the caller

The reusable workflow may be called by more than one caller. A lock only in one caller would not serialize deployments
started by other callers. The lock therefore belongs in the shared deployment workflow; the caller must not declare the
same group.

## Consequences

- Workflow behavior can be shared while application and infrastructure code retain separate ownership.
- Cross-repository checkout is an explicit dependency. Infrastructure scripts and paths must be resolved from the
  infrastructure checkout, while application build commands must use the caller checkout.
- A deployment workflow change in the infrastructure repository can affect all callers that reference it. Changes must
  be reviewed as changes to a shared deployment interface.
- `workflow_call` inputs and secrets form a contract. Changes to required inputs, types, or secret names require updating
  all callers.
- Direct infrastructure triggers preserve validation after the workflows are extracted; reusable workflows alone do
  not run automatically on pull requests or pushes.
- Applying the same concurrency group at both caller and reusable-workflow levels can prevent a deployment from
  starting. Any new deployment caller must rely on the reusable workflow's per-environment lock.
- Workflow YAML and shell syntax checks do not prove AWS access, Terraform plans, cluster access, or a successful
  deployment. These require validation in the appropriate GitHub environment.

## References

- [GitHub Actions: Reusing workflow configurations](https://docs.github.com/actions/sharing-automations/reusing-workflows)
- [GitHub Actions: Concurrency](https://docs.github.com/actions/using-jobs/using-concurrency)
- [ADR-008 — Amazon EKS with Kubernetes YAML and Helm](ADR-008-Amazon-EKS-Runtime.md)
- [AWS deployment and migration runbook](../deployment/aws-migration-runbook.md)
- [Infrastructure repository](https://github.com/1ChristianAlex/motordesk-infrastructure)
