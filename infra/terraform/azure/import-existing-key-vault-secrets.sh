#!/usr/bin/env bash
set -euo pipefail

# Existing Key Vault secrets are adopted into Terraform state before apply.
# This makes deployments resilient when a persistent Key Vault outlives a
# Terraform state or when the state is bootstrapped from an existing environment.

ENVIRONMENT="${1:?environment is required}"
PROJECT_NAME="${2:-motordesk}"
KEY_VAULT_NAME="kv${ENVIRONMENT}-${PROJECT_NAME}"

# Terraform resource address -> Azure Key Vault secret name.
declare -A SECRETS=(
  ["azurerm_key_vault_secret.database_user"]="databaseUser"
  ["azurerm_key_vault_secret.database_password"]="databasePassword"
  ["azurerm_key_vault_secret.mongo_url"]="mongoUrl"
  ["azurerm_key_vault_secret.mongo_user"]="mongoUser"
  ["azurerm_key_vault_secret.mongo_password"]="mongoPassword"
  ["azurerm_key_vault_secret.mongo_database"]="mongoDatabase"
  ["azurerm_key_vault_secret.redis_password"]="redisPassword"
  ["azurerm_key_vault_secret.azure_communication_access_key"]="azureCommunicationAccessKey"
  ["azurerm_key_vault_secret.azure_communication_endpoint"]="azureCommunicationEndpoint"
  ["azurerm_key_vault_secret.jwt_secret"]="jwtSecret"
)

for RESOURCE_ADDRESS in "${!SECRETS[@]}"; do
  SECRET_NAME="${SECRETS[$RESOURCE_ADDRESS]}"

  if terraform state show "$RESOURCE_ADDRESS" >/dev/null 2>&1; then
    echo "State already contains $RESOURCE_ADDRESS; skipping import."
    continue
  fi

  SECRET_ID=$(az keyvault secret show \
    --vault-name "$KEY_VAULT_NAME" \
    --name "$SECRET_NAME" \
    --query id \
    --output tsv 2>/dev/null || true)

  if [[ -z "$SECRET_ID" ]]; then
    echo "Secret '$SECRET_NAME' does not exist in Key Vault '$KEY_VAULT_NAME'; Terraform will create it."
    continue
  fi

  echo "Importing existing secret '$SECRET_NAME' into $RESOURCE_ADDRESS."
  terraform import -input=false "$RESOURCE_ADDRESS" "$SECRET_ID"
done
