# ADR-009 — New Relic for Online Observability and Grafana for Local Use

- **Status:** Accepted — Kubernetes configuration is implemented; online deployment and account-level ingestion still require validation.
- **Decision date:** 2026-11-02

## Context

Motor Desk needs observability for its online AWS environments and a simple option for local development. The backend
already uses OpenTelemetry for application telemetry, and AWS CloudWatch is configured for EKS control-plane and
Kubernetes log collection. The project should extend these responsibilities without instrumenting the application twice,
duplicating log ingestion, or introducing an unnecessarily expensive monitoring stack.

The project is maintained in a student context with a reduced infrastructure budget. At the same time, the developer has
professional experience with New Relic. That familiarity lowers the learning and operational effort for online
monitoring, dashboards, and troubleshooting. These factors justify selecting a managed platform for online use while
keeping local development simple and inexpensive.

## Decision

- Use **New Relic for online environments** (`dev`, `qa`, and `prod`) as the centralized destination for Kubernetes
  infrastructure telemetry and the application's existing OpenTelemetry export.
- Use **Grafana OSS on localhost as the preferred simple local visualization option** when local dashboards are needed.
  Grafana is a development convenience, not an online production platform or a required runtime dependency. The
  repository does not currently provision a local Grafana/Prometheus stack; adding one should be limited to a concrete
  local-development need.
- Keep telemetry responsibilities separate:
  - **OpenTelemetry** remains the application's vendor-independent instrumentation and export mechanism.
  - **New Relic Kubernetes integration** collects Kubernetes infrastructure metrics and events.
  - **CloudWatch** remains the AWS-native destination for EKS control-plane and Kubernetes logs.
  - **Grafana** provides local dashboard visualization only; it does not replace online New Relic or CloudWatch.
- Install New Relic using the official, version-pinned `newrelic/nri-bundle` Helm chart. The current release is
  `newrelic-kubernetes` in namespace `newrelic`, using chart version `8.0.40` and the Terraform-provided EKS cluster name
  for each environment.
- Enable infrastructure metrics, bundled kube-state-metrics, and Kubernetes events. Keep low-data mode enabled.
  Prometheus collection, metadata injection, New Relic logging, Fargate support, and control-plane scraping remain
  disabled unless a reviewed requirement changes.
- Source the New Relic Kubernetes license key only from the GitHub Actions secret `NEW_RELIC_EKS_LICENSE_KEY`. The
  deployment workflow synchronizes it through standard input into the KMS-encrypted Kubernetes Secret
  `newrelic/newrelic-license` (`license-key` data key). Helm values reference the Secret and never contain the credential.
- Do not enable New Relic log forwarding alongside the existing CloudWatch collector without first designing and
  validating a filtered, non-duplicating dual-export pipeline.

## Why New Relic online

New Relic provides a managed place to correlate the existing OpenTelemetry application telemetry with Kubernetes
infrastructure metrics and events. This avoids operating a separate online metrics backend and dashboard stack for the
current small project. The developer's professional familiarity with New Relic makes it practical to implement and
troubleshoot the integration with the available project capacity.

This is a project-specific operational choice, not a claim that New Relic is universally superior to other observability
platforms. The student status and reduced budget make ingestion controls essential: low-data mode is enabled, arbitrary
Kubernetes labels and annotations are not exported, and duplicate Kubernetes log forwarding is disabled. New Relic
account pricing, quotas, and retention are plan-specific and must be confirmed by an authorized account administrator;
the architecture assumes no particular free tier, discount, or fixed retention period.

## Alternatives considered

### CloudWatch only

**Advantages**

- Already configured for EKS control-plane and Kubernetes log collection.
- Avoids adding another vendor's infrastructure agents and ingestion costs.

**Disadvantages**

- Does not use the developer's New Relic operational experience or the existing New Relic OTLP destination as the
  project's unified application and infrastructure observability surface.
- Would require a separate decision and setup for the desired Kubernetes dashboards and event exploration.

**Decision:** Retain CloudWatch for AWS-native logs, but use New Relic for online Kubernetes metrics/events and centralized
application observability.

### Self-managed Prometheus and Grafana online

**Advantages**

- Open tooling, flexible dashboards, and control over where metrics are stored.
- Grafana OSS can be used without a hosted dashboard subscription.

**Disadvantages**

- The project would need to deploy, secure, upgrade, back up, and operate the metrics backend and its storage in every
  online environment.
- Storage, retention, alerting, high availability, and maintenance introduce ongoing infrastructure and engineering
  costs even if the software itself is open source.
- A second online telemetry backend would not remove the need to manage New Relic's existing application OTLP use.

**Decision:** Do not operate this stack online for the current project. Keep Grafana OSS as a simple localhost option.

### Grafana Cloud or another managed observability vendor

**Advantages**

- Managed dashboards, metrics, and potentially logs with integrations for OpenTelemetry.

**Disadvantages**

- Adds a different managed service and account/billing model without a demonstrated advantage over the existing New Relic
  familiarity and OTLP setup.
- A second vendor can increase configuration and data-routing complexity.

**Decision:** Not selected for the current online environments. Reconsider if New Relic cost, retention, or feature limits
no longer fit.

### New Relic logging for Kubernetes

**Advantages**

- Could make logs available alongside New Relic metrics and application telemetry.

**Disadvantages**

- The CloudWatch Observability EKS add-on already collects Kubernetes logs. Enabling another collector without explicit
  routing and filtering risks duplicate ingestion, extra cost, and accidental export of sensitive data.

**Decision:** Disabled. CloudWatch remains the Kubernetes/control-plane log destination unless a future dual-forwarding
design is reviewed.

## Consequences

### Positive

- Online Kubernetes metrics/events and existing OpenTelemetry application signals can be explored in New Relic without
  adding a separate self-managed online dashboard stack.
- Existing CloudWatch log collection remains unchanged.
- Local dashboard exploration can use Grafana OSS without making it part of production infrastructure.
- The pinned Helm release and shared environment configuration make deployments repeatable.
- Low-data mode and a restricted Kubernetes label allowlist help constrain ingestion volume and metadata exposure.

### Negative and risks

- New Relic introduces vendor dependence and account-level ingestion costs; reduced student-budget assumptions must be
  checked against actual pricing and usage.
- New Relic retention is governed by the selected account plan and is not guaranteed by this repository. CloudWatch
  control-plane log retention is configured to 30 days; container-log retention should be verified in each AWS account.
- New Relic agents consume node resources and require outbound HTTPS access to New Relic ingestion endpoints.
- Grafana is not currently provisioned locally. Developers wanting local dashboards need to choose and configure a local
  data source; this ADR does not promise that local New Relic account data is available without an explicit secure
  configuration.
- No live deployment, agent readiness, or New Relic UI ingestion is implied by chart validation alone.

## Revisit criteria

Reassess this decision if New Relic usage exceeds the approved student/project budget; account retention or quotas do not
meet operational needs; the application or cluster grows enough to change ingestion/resource costs; privacy or compliance
requirements change; the project needs reliable offline/local dashboards; a maintained local Prometheus/Grafana stack
becomes an explicit requirement; or a different provider offers a material operational or financial advantage. Any
change to log forwarding must also establish filtering, sensitive-field masking, trace/span correlation, and duplicate
ingestion controls before enabling another collector.

## References

- [New Relic Kubernetes installation](https://docs.newrelic.com/install/kubernetes/)
- [New Relic Helm charts](https://github.com/newrelic/helm-charts)
- [`nri-bundle` chart documentation](https://github.com/newrelic/helm-charts/tree/master/charts/nri-bundle)
- [`nri-bundle` values](https://github.com/newrelic/helm-charts/blob/master/charts/nri-bundle/values.yaml)
- [OpenTelemetry](https://opentelemetry.io/docs/)
- [Grafana OSS](https://grafana.com/oss/grafana/)
- [AWS and Kubernetes deployment architecture](../deployment/aws-terraform-architecture.md)
- [New Relic Kubernetes observability runbook](../../infra/kubernetes/newrelic/README.md)
- [ADR-008 — Amazon EKS with Kubernetes YAML and Helm](ADR-008-Amazon-EKS-Runtime.md)
