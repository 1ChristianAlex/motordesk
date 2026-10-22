data "azurerm_app_configuration_key" "database_url" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "DATABASE_URL"
  label                  = var.environment
  depends_on             = [azurerm_app_configuration_key.ack_database_url]
}

data "azurerm_app_configuration_key" "database_driver" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "DATABASE_DRIVER"
  label                  = var.environment
  depends_on             = [azurerm_app_configuration_key.ack_database_driver]
}

data "azurerm_app_configuration_key" "jwt_issuer" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "JWT_ISSUER"
  label                  = var.environment
  depends_on             = [azurerm_app_configuration_key.ack_jwt_issuer]
}

data "azurerm_app_configuration_key" "jwt_audience" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "JWT_AUDIENCE"
  label                  = var.environment
  depends_on             = [azurerm_app_configuration_key.ack_jwt_audience]
}

data "azurerm_app_configuration_key" "jwt_realm" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "JWT_REALM"
  label                  = var.environment
  depends_on             = [azurerm_app_configuration_key.ack_jwt_realm]
}
