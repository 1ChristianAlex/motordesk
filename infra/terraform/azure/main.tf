# We strongly recommend using the required_providers block to set the
# Azure Provider source and version being used
terraform {
  backend "azurerm" {}

  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "5.3.0"
    }
    azapi = {
      source  = "Azure/azapi"
      version = "2.12.0"
    }
    random = {
      source  = "hashicorp/random"
      version = "3.9.0"
    }
  }
}

provider "random" {
  # Configuration options
}

provider "azurerm" {
  features {
    key_vault {
      purge_soft_deleted_secrets_on_destroy = false
      recover_soft_deleted_secrets          = false
    }

    app_configuration {
      purge_soft_delete_on_destroy = false
      recover_soft_deleted         = false
    }
  }
}

provider "azapi" {
  skip_provider_registration = false
}

data "azurerm_client_config" "client_config" {}

resource "azurerm_resource_group" "rg" {
  name     = "terraform-${var.environment}-${var.project_name}"
  location = var.infra_location
  tags     = var.tags
}

resource "azurerm_app_configuration" "app_conf" {
  name                = "appConf-${var.environment}-${var.project_name}"
  location            = azurerm_resource_group.rg.location
  resource_group_name = azurerm_resource_group.rg.name

  depends_on = [azurerm_resource_group.rg]
}

resource "azurerm_role_assignment" "appconf_dataowner" {
  scope                = azurerm_app_configuration.app_conf.id
  role_definition_name = "App Configuration Data Owner"
  principal_id         = data.azurerm_client_config.client_config.object_id
}

# The Key Vault is a pre-existing application resource.
# It is intentionally read as a data source so the application Terraform
# apply does not try to create or destroy the vault itself.
data "azurerm_key_vault" "kvault_app" {
  name                = "kv${var.environment}-${var.project_name}"
  resource_group_name = azurerm_resource_group.rg.name
}

# The pipeline identity is a subscription/resource-group Contributor, but
# Contributor does not grant access to Key Vault secrets. Keep the vault in
# access-policy mode for now and grant only the read permissions Terraform
# needs to consume existing application secrets. A later migration can move
# this policy to Azure RBAC independently.
resource "azurerm_key_vault_access_policy" "terraform" {
  key_vault_id = data.azurerm_key_vault.kvault_app.id
  tenant_id    = data.azurerm_client_config.client_config.tenant_id
  object_id    = data.azurerm_client_config.client_config.object_id

  secret_permissions = [
    "Get",
    "List",
  ]
}

# $Env:ARM_CLIENT_ID = ""
# $Env:ARM_CLIENT_SECRET = ""
# $Env:ARM_SUBSCRIPTION_ID = ""
# $Env:ARM_TENANT_ID = ""
