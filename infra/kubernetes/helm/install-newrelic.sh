#!/usr/bin/env bash
set -euo pipefail
set +x

: "${EKS_CLUSTER_NAME:?EKS_CLUSTER_NAME is required}"
: "${NEW_RELIC_EKS_LICENSE_KEY:?Set NEW_RELIC_EKS_LICENSE_KEY in the GitHub Environment or repository secrets.}"

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
VALUES_FILE="$SCRIPT_DIR/../newrelic/values.yaml"
source "$SCRIPT_DIR/versions.env"

helm repo add newrelic https://helm-charts.newrelic.com --force-update
helm repo update newrelic

kubectl create namespace newrelic --dry-run=client --output=yaml | kubectl apply -f -
printf '%s' "$NEW_RELIC_EKS_LICENSE_KEY" |
  kubectl create secret generic newrelic-license \
    --namespace newrelic \
    --from-file=license-key=/dev/stdin \
    --dry-run=client \
    --output=yaml |
  kubectl apply -f -
unset NEW_RELIC_EKS_LICENSE_KEY

helm upgrade --install newrelic-kubernetes newrelic/nri-bundle \
  --version "$NEW_RELIC_NRI_BUNDLE_CHART_VERSION" \
  --namespace newrelic \
  --values "$VALUES_FILE" \
  --set-string "global.cluster=$EKS_CLUSTER_NAME" \
  --wait \
  --atomic \
  --timeout 10m \
  --history-max 10
