resource "azurerm_container_registry" "acr" {
  name                = "${var.environment}${var.project_name}acr"
  resource_group_name = azurerm_resource_group.rg.name
  location            = azurerm_resource_group.rg.location
  sku                 = "Basic"
  admin_enabled       = false
  tags                = var.tags
}

resource "azurerm_container_registry_scope_map" "scope_map" {
  name                    = "${var.environment}${var.project_name}acr-scope-map"
  container_registry_name = azurerm_container_registry.acr.name
  resource_group_name     = azurerm_resource_group.rg.name
  actions = [
    "repositories/repo1/content/read",
    "repositories/repo1/content/write"
  ]
}

resource "azurerm_container_registry_token" "acr_token" {
  name                    = "${var.environment}${var.project_name}acr-token"
  container_registry_name = azurerm_container_registry.acr.name
  resource_group_name     = azurerm_resource_group.rg.name
  scope_map_id            = azurerm_container_registry_scope_map.scope_map.id
}

data "azurerm_container_registry_token" "acr_token" {
  name                    = azurerm_container_registry_token.acr_token.name
  container_registry_name = azurerm_container_registry_token.acr_token.container_registry_name
  resource_group_name     = azurerm_container_registry_token.acr_token.resource_group_name
  scope_map_id            = azurerm_container_registry_token.acr_token.scope_map_id
}
