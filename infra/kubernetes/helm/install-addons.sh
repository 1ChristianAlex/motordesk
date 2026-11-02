#!/usr/bin/env bash
set -euo pipefail

: "${AWS_REGION:?AWS_REGION is required}"
: "${EKS_CLUSTER_NAME:?EKS_CLUSTER_NAME is required}"
: "${EKS_KUBERNETES_VERSION:?EKS_KUBERNETES_VERSION is required}"
: "${EKS_VPC_ID:?EKS_VPC_ID is required}"
: "${AWS_LOAD_BALANCER_CONTROLLER_ROLE_ARN:?AWS_LOAD_BALANCER_CONTROLLER_ROLE_ARN is required}"

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/versions.env"

helm repo add eks https://aws.github.io/eks-charts
helm repo add external-secrets https://charts.external-secrets.io
helm repo add metrics-server https://kubernetes-sigs.github.io/metrics-server/
helm repo add autoscaler https://kubernetes.github.io/autoscaler
helm repo add external-dns https://kubernetes-sigs.github.io/external-dns/
helm repo update

helm upgrade --install external-secrets external-secrets/external-secrets \
  --version "$EXTERNAL_SECRETS_CHART_VERSION" \
  --namespace external-secrets \
  --create-namespace \
  --set replicaCount=1 \
  --set serviceAccount.create=true \
  --set serviceAccount.name=external-secrets \
  --wait \
  --timeout 10m

helm upgrade --install metrics-server metrics-server/metrics-server \
  --version "$METRICS_SERVER_CHART_VERSION" \
  --namespace kube-system \
  --wait \
  --timeout 10m

helm upgrade --install aws-load-balancer-controller eks/aws-load-balancer-controller \
  --version "$AWS_LOAD_BALANCER_CONTROLLER_CHART_VERSION" \
  --namespace kube-system \
  --set clusterName="$EKS_CLUSTER_NAME" \
  --set region="$AWS_REGION" \
  --set vpcId="$EKS_VPC_ID" \
  --set serviceAccount.create=true \
  --set serviceAccount.name=aws-load-balancer-controller \
  --set-string "serviceAccount.annotations.eks\\.amazonaws\\.com/role-arn=$AWS_LOAD_BALANCER_CONTROLLER_ROLE_ARN" \
  --wait \
  --timeout 10m

helm upgrade --install cluster-autoscaler autoscaler/cluster-autoscaler \
  --version "$CLUSTER_AUTOSCALER_CHART_VERSION" \
  --namespace kube-system \
  --set autoDiscovery.clusterName="$EKS_CLUSTER_NAME" \
  --set awsRegion="$AWS_REGION" \
  --set image.tag="v${EKS_KUBERNETES_VERSION}.0" \
  --set rbac.create=true \
  --set rbac.serviceAccount.create=true \
  --set rbac.serviceAccount.name=cluster-autoscaler \
  --wait \
  --timeout 10m

if [[ -n "${ROUTE53_ZONE_ID:-}" && -n "${API_DOMAIN_NAME:-}" ]]; then
  : "${EXTERNAL_DNS_ROLE_ARN:?EXTERNAL_DNS_ROLE_ARN is required when Route 53 is configured}"

  helm upgrade --install external-dns external-dns/external-dns \
    --version "$EXTERNAL_DNS_CHART_VERSION" \
    --namespace kube-system \
    --set provider.name=aws \
    --set serviceAccount.create=true \
    --set serviceAccount.name=external-dns \
    --set-string "serviceAccount.annotations.eks\\.amazonaws\\.com/role-arn=$EXTERNAL_DNS_ROLE_ARN" \
    --set "domainFilters={$API_DOMAIN_NAME}" \
    --set "extraArgs={--aws-zone-type=public,--zone-id-filter=$ROUTE53_ZONE_ID,--policy=upsert-only}" \
    --wait \
    --timeout 10m
fi
