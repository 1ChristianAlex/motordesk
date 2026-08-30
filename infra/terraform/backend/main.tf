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
  description = "Project name used as a resource prefix."
  type        = string
  default     = "motordesk"
}

variable "environment" {
  description = "Environment used to isolate the Terraform state backend."
  type        = string
  default     = "dev"
}

variable "infra_location" {
  description = "Azure region for the persistent Terraform backend resources."
  type        = string
  default     = "brazilsouth"
}

variable "tags" {
  description = "Tags applied to persistent Terraform backend resources."
  type        = map(string)

  default = {
    project     = "motordesk"
    managed_by  = "terraform"
    environment = "dev"
    purpose     = "terraform-state"
  }
}

resource "azurerm_resource_group" "resource_persistent" {
  name     = "rg-persistent-${var.project_name}-${var.environment}"
  location = var.infra_location
  tags     = var.tags
}

resource "azurerm_storage_account" "tfstate_storage_account" {
  name                            = lower(replace("st${var.project_name}${var.environment}tfstate", "-", ""))
  resource_group_name             = azurerm_resource_group.resource_persistent.name
  location                        = azurerm_resource_group.resource_persistent.location
  account_tier                    = "Standard"
  account_replication_type        = "LRS"
  min_tls_version                 = "TLS1_2"
  https_traffic_only_enabled      = true
  allow_nested_items_to_be_public = false
  tags                            = var.tags
}

resource "azurerm_storage_container" "tfstate_storage_account" {
  name               = "tfstate"
  storage_account_id = azurerm_storage_account.tfstate_storage_account.id
  container_access_type = "private"
}

output "resource_group_name" {
  description = "Resource group containing the Terraform state storage."
  value       = azurerm_resource_group.resource_persistent.name
}

output "storage_account_name" {
  description = "Storage account containing the Terraform state."
  value       = azurerm_storage_account.tfstate_storage_account.name
}

output "container_name" {
  description = "Blob container containing Terraform state files."
  value       = azurerm_storage_container.tfstate_storage_account.name
}
