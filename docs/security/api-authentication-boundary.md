# API authentication boundary

The internet-facing ALB is protected by an AWS WAFv2 web ACL provisioned by
`infra/terraform/aws/modules/application/waf.tf` and attached to the Ingress by
`infra/kubernetes/deploy-workloads.sh`. The ACL has an explicit route allowlist:
public routes are forwarded without credentials, classified protected routes
require an `Authorization` value beginning with `Bearer `, and all other
method/path combinations receive HTTP 401 at the edge. The allowlist must be
updated together with Ktor routes.

| Classification | Method | Path | Ktor authentication |
|---|---|---|---|
| Public | `GET` | `/health` | None; ALB and Kubernetes health checks |
| Public | `GET` | `/openapi` | OpenAPI document |
| Public | `GET` | `/swaggerUI` and `/swaggerUI/*` | Swagger UI and its resources |
| Public | `POST` | `/login` | None |
| Public | `POST` | `/register/client` | None |
| Public | `GET` | `/service-order/client/{code}/approval?token={token}` | Tokenized approval flow |
| Protected | `POST` | `/register/manager` | `Route.manager` |
| Protected | `POST` | `/service-order/manager/create` | `Route.manager` |
| Protected | `GET` | `/service-order/manager/list` | `Route.manager` |
| Protected | `GET` | `/service-order/manager/{code}` | `Route.manager` |
| Protected | `DELETE` | `/service-order/manager/{code}/delete` | `Route.manager` |
| Protected | `PUT` | `/service-order/manager/update` | `Route.engineer` |
| Protected | `GET` | `/service-order/client` | `Route.client` |
| Protected | `GET` | `/service-order/client/{code}` | `Route.client` |
| Protected | `GET` | `/self` | `Route.client` |
| Protected | `PUT` | `/self/update` | `Route.client` |
| Protected | `GET` | `/vehicles/owner` | `Route.client` |
| Protected | `POST` | `/vehicles/create` | `Route.manager` |
| Protected | `PUT` | `/vehicles/update` | `Route.manager` |
| Protected | `DELETE` | `/vehicles/delete/{id}` | `Route.manager` |
| CORS preflight | `OPTIONS` | Any path | Handled by Ktor CORS; no business operation |

The approval token is a query parameter; the WAF matches its path and method,
not the token value. Public path and method matching is exact. Trailing-slash,
unknown-path, and unknown-method variants are not implicitly public. CORS
preflight requests are allowed because `OPTIONS` does not invoke a business
operation. The Swagger UI and OpenAPI endpoints are also public because the
application registers them independently of the authenticated controllers.

The WAF check is an early filter, not authentication: it only checks the
`Bearer ` scheme on known protected routes. Ktor remains responsible for
signature, algorithm, issuer, audience, time claims, required claims, roles,
and resource ownership. A fabricated, expired, or otherwise invalid bearer
value reaches Ktor and must still be rejected there. Do not use client-supplied
identity or role headers.

The web ACL uses a custom HTTP 401 response with the non-sensitive
`X-MotorDesk-Edge-Auth: required` header so deployment smoke checks can
distinguish an edge rejection from a Ktor authentication failure. WAF CloudWatch
metrics are enabled, but sampled requests and WAF request logging are disabled
to avoid collecting Authorization values. The health path remains public to
the ALB health checker; Kubernetes startup, readiness, and liveness probes
continue to reach the pod directly inside the cluster. The API Service remains
`ClusterIP`, and the Ingress uses ALB IP targets.

The declared EKS node group uses private application subnets, so the API has no
public node address or NodePort path in the manifests. The load balancer
controller manages the ALB-to-target security-group rules. Terraform separately
exposes the EKS control-plane endpoint to `0.0.0.0/0` for deployment-runner
access; IAM authentication and EKS authorization are still required. This is a
management-plane exposure, not an alternate application API path, and its
source CIDR restriction remains a separate hardening task.

AWS WAF is billed for the web ACL, rules, and inspected requests; review current
regional pricing and expected request volume before deployment. The existing
Ingress continues to use HTTP when no custom domain and ACM certificate are
configured. Use the optional domain/certificate configuration for HTTPS; the
ALB DNS name alone does not provide TLS in the current deployment.

After deployment, the workflow checks `/health` and verifies that a protected
Service Order route returns the edge marker with no Authorization header and
with a non-Bearer scheme. It also checks that a syntactically present but
invalid Bearer value passes the edge and is rejected by Ktor with HTTP 401.
The deployment uses `WAF_WEB_ACL_ARN` from the environment's Terraform output;
the Ingress must not be applied with an empty ACL ARN.
