# We strongly recommend using the required_providers block to set the
# Azure Provider source and version being used
terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "~>5.0"
    }
    azapi = {
      source  = "Azure/azapi"
      version = "2.12.0"
    }
  }
}

# Configure the Microsoft Azure Provider
provider "azurerm" {
  features {}
}

provider "azapi" {
  skip_provider_registration = false
}

data "azurerm_client_config" "client_config" {}

# Create a resource group
resource "azurerm_resource_group" "rg" {
  name     = "terraform-${var.environment}-${var.project_name}"
  location = var.infra_location
  tags     = var.tags
}

resource "azurerm_role_assignment" "current_user" {
  scope                = azurerm_resource_group.rg.id
  role_definition_name = "Terraform Integration"
  principal_id         = data.azurerm_client_config.client_config.object_id
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
