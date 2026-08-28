# We strongly recommend using the required_providers block to set the
# Azure Provider source and version being used
terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "5.3.0"
    }
    azapi = {
      source  = "Azure/azapi"
      version = "2.12.0"
    }
  }
}

# Configure the Microsoft Azure Provider
provider "azurerm" {
  features {
    key_vault {
      purge_soft_deleted_secrets_on_destroy = true
      recover_soft_deleted_secrets          = true
    }

    app_configuration {
      purge_soft_delete_on_destroy = true
      recover_soft_deleted         = true
    }
  }
}

provider "azapi" {
  skip_provider_registration = false
}

data "azurerm_client_config" "client_config" {

}


# Create a resource group
resource "azurerm_resource_group" "rg" {
  name     = "terraform-${var.environment}-${var.project_name}"
  location = var.infra_location
  tags     = var.tags
}

resource "azurerm_app_configuration" "app_conf" {
  name                       = "appConf-${var.environment}-${var.project_name}"
  location                   = azurerm_resource_group.rg.location
  resource_group_name        = azurerm_resource_group.rg.name

  depends_on = [ azurerm_resource_group.rg ]
}

resource "azurerm_role_assignment" "appconf_dataowner" {
  scope                = azurerm_app_configuration.app_conf.id
  role_definition_name = "App Configuration Data Owner"
  principal_id         = data.azurerm_client_config.client_config.object_id
}

resource "azurerm_key_vault" "key_vault_app" {
  name                       = "kv${var.environment}-${var.project_name}"
  location                   = azurerm_resource_group.rg.location
  resource_group_name        = azurerm_resource_group.rg.name
  rbac_authorization_enabled = false
  tenant_id                  = data.azurerm_client_config.client_config.tenant_id
  sku_name                   = "premium"
  soft_delete_retention_days = 7
  tags                       = var.tags

  access_policy {
    tenant_id = data.azurerm_client_config.client_config.tenant_id
    object_id = data.azurerm_client_config.client_config.object_id

    key_permissions = [
      "Create",
      "Delete",
      "Get",
      "Purge",
      "Recover",
      "List",
      "Update",
      "GetRotationPolicy",
      "SetRotationPolicy"
    ]

    secret_permissions = [
      "Set",
      "Get",
      "Delete",
      "List",
      "Purge",
      "Recover"
    ]
  }
}



# Create a virtual network within the resource group
resource "azurerm_virtual_network" "network" {
  name                = "network-${var.environment}-${var.project_name}"
  resource_group_name = azurerm_resource_group.rg.name
  location            = var.infra_location
  tags                = var.tags
  address_space       = ["10.20.0.0/16"]
}


# $Env:ARM_CLIENT_ID = ""
# $Env:ARM_CLIENT_SECRET = ""
# $Env:ARM_SUBSCRIPTION_ID = ""
# $Env:ARM_TENANT_ID = ""
