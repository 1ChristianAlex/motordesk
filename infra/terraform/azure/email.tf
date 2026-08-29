resource "azurerm_communication_service" "cs_email" {
  name                = var.resource_name_email
  resource_group_name = azurerm_resource_group.rg.name
  data_location       = "United States"

  depends_on = [azurerm_resource_group.rg]
}

resource "azurerm_email_communication_service" "cs_email_service" {
  name                = var.resource_name_email
  resource_group_name = azurerm_resource_group.rg.name
  data_location       = "United States"

}

resource "azurerm_email_communication_service_domain" "cs_email_domain" {
  name              = "AzureManagedDomain"
  email_service_id  = azurerm_email_communication_service.cs_email_service.id
  domain_management = "AzureManaged"
}

resource "azurerm_communication_service_email_domain_association" "cs_email_domain_link" {
  communication_service_id = azurerm_communication_service.cs_email.id
  email_service_domain_id  = azurerm_email_communication_service_domain.cs_email_domain.id
}

resource "azurerm_email_communication_service_domain_sender_username" "cs_email_sender" {
  name                    = var.no_replay_username
  email_service_domain_id = azurerm_email_communication_service_domain.cs_email_domain.id
  display_name            = "${var.project_name} email"
}

data "azurerm_communication_service" "communication_service" {
  name                = var.resource_name_email
  resource_group_name = azurerm_resource_group.rg.name

  depends_on = [azurerm_communication_service.cs_email]
}
