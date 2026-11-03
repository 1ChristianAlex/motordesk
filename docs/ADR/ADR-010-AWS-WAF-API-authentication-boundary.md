# ADR-010 — AWS WAF API authentication boundary

- **Status:** Accepted
- **Decision date:** 2026-11-02

## Context

The API is exposed through an internet-facing ALB created by AWS Load Balancer
Controller. The current Ingress has a catch-all `/` forward route. ALB listener
conditions are positive matches; a missing-header condition does not provide a
safe negative match on this catch-all route, and the current forward action
would otherwise continue sending unmatched requests to Ktor. An edge control
must reject missing and non-Bearer credentials without treating a present
header as a valid JWT.

Ktor currently owns JWT verification and authorization. The route classification
is derived from the route groups used in controllers (`manager`, `engineer`,
and `client`), with login, client registration, health, the registered Swagger
and OpenAPI routes, and tokenized service order approval remaining public.

## Decision

- Provision one regional AWS WAFv2 Web ACL per Terraform environment and attach
  it to the ALB through the `alb.ingress.kubernetes.io/wafv2-acl-arn`
  annotation.
- Explicitly allow the documented public method/path combinations.
- Forward only documented protected method/path combinations with an
  `Authorization` value beginning with `Bearer `.
- Allow `OPTIONS` requests for CORS preflight; they do not invoke business
  operations.
- Return HTTP 401 for all other requests. This includes unknown routes and
  methods, so adding an API route requires updating the WAF allowlist and route
  inventory.
- Keep JWT validation, role checks, and resource ownership in Ktor. WAF does
  not validate token contents, claims, or identity.
- Enable non-sensitive WAF CloudWatch metrics, but disable sampled requests and
  do not configure WAF request logging.
- Keep the API Service as `ClusterIP` and the Ingress target type as `ip`; the
  ALB remains the intended public API entry point.

## Alternatives considered

- **ALB listener rules only:** rejected for the current catch-all Ingress.
  Listener conditions can positively match a header, but the existing default
  forward route does not safely reject the requests that fail that match. A
  complete ALB-only design would require replacing the current routing model
  with an equally explicit allowlist and fallback response.
- **AWS WAFv2:** selected because it supports explicit method/path/header
  statements and a default block response on the existing ALB without
  introducing another proxy or changing the application entry point.
- **API Gateway or a separate proxy:** rejected because it would replace or
  duplicate the EKS/ALB ingress architecture and add another operational
  component.

## Consequences

- Every environment adds AWS WAF charges for the Web ACL, its rules, and
  inspected requests. Verify current regional pricing before applying.
- WAF route expressions are a second representation of the Ktor route
  contract. The security route inventory and WAF rules must be updated together.
- The default edge response uses HTTP 401 and a static diagnostic response
  header. The header does not contain request or credential data.
- Health checks remain public. AWS WAF sampled requests and request logging
  remain disabled to prevent Authorization header values from being captured.
- TLS behavior is unchanged: HTTPS and HTTP-to-HTTPS redirection are configured
  only when the custom domain and ACM certificate are supplied. Without them,
  the ALB DNS endpoint remains HTTP-only.
