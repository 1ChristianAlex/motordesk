terraform {
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

variable "project_name" {
  type    = string
  default = "motordesk"
}

variable "environment" {
  type    = string
  default = "dev"
}

resource "azurerm_resource_group" "tfstate" {
  name     = "rg-tfstate-${var.project_name}"
  location = "brazilsouth"
}

resource "azurerm_storage_account" "tfstate" {
  name                            = lower(replace("st${var.project_name}${var.environment}tfstate", "-", ""))
  resource_group_name             = azurerm_resource_group.tfstate.name
  location                        = azurerm_resource_group.tfstate.location
  account_tier                    = "Standard"
  account_replication_type        = "LRS"
  min_tls_version                 = "TLS1_2"
  allow_nested_items_to_be_public = false
}

resource "azurerm_storage_container" "tfstate" {
  name                  = "tfstate"
  storage_account_id    = azurerm_storage_account.tfstate.id
  container_access_type = "private"
}

output "resource_group_name" {
  value = azurerm_resource_group.tfstate.name
}

output "storage_account_name" {
  value = azurerm_storage_account.tfstate.name
}

output "container_name" {
  value = azurerm_storage_container.tfstate.name
}
