#!/usr/bin/env bash
set -euo pipefail

: "${AWS_REGION:?AWS_REGION is required}"
: "${ENVIRONMENT:?ENVIRONMENT is required}"
: "${ECR_REPOSITORY_URL:?ECR_REPOSITORY_URL is required}"
: "${IMAGE_TAG:?IMAGE_TAG is required}"
: "${API_CERTIFICATE_ARN:=}"
: "${API_DOMAIN_NAME:=}"
: "${ROUTE53_ZONE_ID:=}"
: "${EFS_FILE_SYSTEM_ID:?EFS_FILE_SYSTEM_ID is required}"
: "${EFS_ACCESS_POINT_ID:?EFS_ACCESS_POINT_ID is required}"
: "${POSTGRES_SECRET_ARN:?POSTGRES_SECRET_ARN is required}"
: "${REDIS_SECRET_ARN:?REDIS_SECRET_ARN is required}"
: "${JWT_SECRET_ARN:?JWT_SECRET_ARN is required}"
: "${MONGO_SECRET_ARN:?MONGO_SECRET_ARN is required}"
: "${PARAMETER_STORE_PREFIX:?PARAMETER_STORE_PREFIX is required}"
export MONGO_OPTIONS="${MONGO_OPTIONS:-}"

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
BASE_DIR="$SCRIPT_DIR/base"

kubectl apply -f "$BASE_DIR/namespace.yaml"
if ! kubectl create service clusterip motordesk-webhook-preflight \
  --tcp=80:80 --namespace motordesk --dry-run=server >/dev/null; then
  echo "::error::AWS Load Balancer Controller admission webhook preflight failed."
  kubectl describe mutatingwebhookconfiguration aws-load-balancer-webhook || true
  kubectl logs deployment/aws-load-balancer-controller --namespace kube-system \
    --all-containers=true --tail=100 || true
  exit 1
fi
envsubst '${MONGO_OPTIONS}' < "$BASE_DIR/api-configmap.yaml.tpl" | kubectl apply -f -
kubectl apply -f "$BASE_DIR/aws-serviceaccounts.yaml"

envsubst < "$BASE_DIR/secrets.yaml.tpl" | kubectl apply -f -
envsubst < "$BASE_DIR/mongo-pv.yaml.tpl" | kubectl apply -f -

wait_for_external_secret() {
  local name="$1"
  local store="$2"
  if ! kubectl wait --for=condition=Ready "externalsecret/$name" \
    --namespace motordesk --timeout=5m; then
    echo "::error::ExternalSecret $name did not become Ready; collecting diagnostics."
    kubectl describe externalsecret "$name" --namespace motordesk || true
    kubectl describe secretstore "$store" --namespace motordesk || true
    kubectl get events --namespace motordesk --sort-by=.lastTimestamp | tail -n 40 || true
    exit 1
  fi
}

wait_for_external_secret motordesk-runtime aws-secrets-manager
wait_for_external_secret motordesk-parameters aws-parameter-store

kubectl apply -f "$BASE_DIR/mongo.yaml"
if ! kubectl rollout status statefulset/mongo --namespace motordesk --timeout=15m; then
  echo "::error::MongoDB StatefulSet did not become ready; collecting diagnostics."
  kubectl get pods,pvc -n motordesk -o wide || true
  kubectl describe pod mongo-0 -n motordesk || true
  kubectl describe pvc data-mongo-0 -n motordesk || true
  kubectl describe pv mongo-efs-pv || true
  kubectl get events -n motordesk --sort-by=.lastTimestamp | tail -n 40 || true
  exit 1
fi

kubectl apply -f "$BASE_DIR/api.yaml"
kubectl set image deployment/motordesk-api \
  "api=$ECR_REPOSITORY_URL:$IMAGE_TAG" \
  --namespace motordesk
kubectl apply -f "$BASE_DIR/hpa-$ENVIRONMENT.yaml"

if [[ "$ENVIRONMENT" == "prod" ]]; then
  kubectl apply -f "$BASE_DIR/pdb-prod.yaml"
fi

kubectl rollout status deployment/motordesk-api --namespace motordesk --timeout=10m
kubectl apply -f "$BASE_DIR/ingress.yaml"

if [[ -n "$API_DOMAIN_NAME" ]]; then
  if [[ -z "$API_CERTIFICATE_ARN" ]]; then
    echo "::error::A custom API domain requires a validated ACM certificate."
    exit 1
  fi
  kubectl annotate ingress motordesk-api --namespace motordesk \
    alb.ingress.kubernetes.io/certificate-arn="$API_CERTIFICATE_ARN" \
    alb.ingress.kubernetes.io/listen-ports='[{"HTTP":80},{"HTTPS":443}]' \
    alb.ingress.kubernetes.io/ssl-redirect=443 \
    --overwrite
  kubectl patch ingress motordesk-api --namespace motordesk --type=json \
    --patch "[{\"op\":\"add\",\"path\":\"/spec/rules/0/host\",\"value\":\"$API_DOMAIN_NAME\"}]"

  if [[ -n "${ROUTE53_ZONE_ID:-}" ]]; then
    kubectl annotate ingress motordesk-api --namespace motordesk \
      external-dns.alpha.kubernetes.io/hostname="$API_DOMAIN_NAME" \
      --overwrite
  fi
fi

ingress_hostname=""
for attempt in {1..60}; do
  ingress_hostname="$(kubectl get ingress motordesk-api --namespace motordesk \
    --output=jsonpath='{.status.loadBalancer.ingress[0].hostname}' 2>/dev/null || true)"
  if [[ -n "$ingress_hostname" ]]; then
    break
  fi
  sleep 5
done

if [[ -z "$ingress_hostname" ]]; then
  echo "::error::The AWS Load Balancer Controller did not assign an ALB hostname to the API Ingress."
  exit 1
fi

collect_ingress_diagnostics() {
  echo "::group::Ingress and load balancer diagnostics"
  kubectl describe ingress motordesk-api --namespace motordesk || true
  kubectl get ingress,service,endpoints,endpointslices,targetgroupbindings \
    --namespace motordesk -o wide || true
  kubectl get events --namespace motordesk --sort-by=.lastTimestamp | tail -n 40 || true
  kubectl logs deployment/aws-load-balancer-controller --namespace kube-system \
    --all-containers=true --tail=150 || true
  echo "::endgroup::"
}

echo "Waiting for the load balancer hostname to resolve: $ingress_hostname"
dns_resolved=false
for attempt in {1..60}; do
  if getent ahostsv4 "$ingress_hostname" >/dev/null 2>&1; then
    dns_resolved=true
    break
  fi
  sleep 5
done

if [[ "$dns_resolved" != "true" ]]; then
  echo "::error::The load balancer hostname did not resolve after 5 minutes: $ingress_hostname"
  collect_ingress_diagnostics
  exit 1
fi

if [[ -n "$API_DOMAIN_NAME" ]]; then
  if ! curl --fail --silent --show-error --retry 12 --retry-delay 10 --retry-connrefused \
    --retry-all-errors --max-time 15 \
    --connect-to "$API_DOMAIN_NAME:443:$ingress_hostname:443" \
    "https://$API_DOMAIN_NAME/health"; then
    echo "::error::The API health check failed through the ALB."
    collect_ingress_diagnostics
    exit 1
  fi
else
  if ! curl --fail --silent --show-error --retry 12 --retry-delay 10 --retry-connrefused \
    --retry-all-errors --max-time 15 \
    "http://$ingress_hostname/health"; then
    echo "::error::The API health check failed through the ALB."
    collect_ingress_diagnostics
    exit 1
  fi
fi
