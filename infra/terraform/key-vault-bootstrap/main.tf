terraform {
  backend "azurerm" {}

  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "5.3.0"
    }
  }
}

provider "azurerm" {
  features {}
}

data "azurerm_resource_group" "app" {
  name = "terraform-${var.environment}-${var.project_name}"
}

data "azurerm_client_config" "current" {}

resource "azurerm_key_vault" "app" {
  name                       = "kv${var.environment}-${var.project_name}"
  location                   = data.azurerm_resource_group.app.location
  resource_group_name        = data.azurerm_resource_group.app.name
  rbac_authorization_enabled = true
  tenant_id                  = data.azurerm_client_config.current.tenant_id
  sku_name                   = "premium"
  soft_delete_retention_days = 7
  tags                       = var.tags
}

resource "azurerm_role_assignment" "terraform_secrets" {
  scope                = azurerm_key_vault.app.id
  role_definition_name = "Key Vault Secrets Officer"
  principal_id         = data.azurerm_client_config.current.object_id
}

output "key_vault_id" {
  description = "Key Vault resource ID."
  value       = azurerm_key_vault.app.id
}

output "key_vault_name" {
  description = "Key Vault name."
  value       = azurerm_key_vault.app.name
}
