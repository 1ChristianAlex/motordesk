resource "azapi_resource" "communicationService" {
  type      = "Microsoft.Communication/communicationServices@2026-03-18"
  parent_id = azurerm_resource_group.rg.id
  name      = var.resource_name_email
  tags      = var.tags
  location  = "global"
  body = {
    properties = {
      dataLocation = "United States"
    }
  }
  schema_validation_enabled = false
  response_export_values    = ["*"]
  depends_on                = [azurerm_resource_group.rg]
}

resource "azapi_resource" "emailService" {
  type      = "Microsoft.Communication/emailServices@2026-03-18"
  parent_id = azurerm_resource_group.rg.id
  name      = var.resource_name_email
  location  = "global"
  body = {
    properties = {
      dataLocation = "United States"
    }
  }
  tags                      = var.tags
  schema_validation_enabled = false
  response_export_values    = ["*"]
  depends_on                = [azapi_resource.communicationService]
}

resource "azapi_resource" "email_domain" {
  type      = "Microsoft.Communication/emailServices/domains@2026-03-18"
  name      = "${var.environment}-${var.project_name}.com"
  location  = "global"
  parent_id = azapi_resource.emailService.id
  tags      = var.tags
  body = {
    properties = {
      domainManagement       = "CustomerManaged"
      userEngagementTracking = "Disabled"
    }
  }
}

resource "azapi_resource" "senderUsername" {
  type      = "Microsoft.Communication/emailServices/domains/senderUsernames@2026-03-18"
  name      = var.no_replay_username
  parent_id = azapi_resource.email_domain.id
  body = {
    properties = {
      displayName = "${var.project_name} email"
      username    = var.no_replay_username
    }
  }
}
