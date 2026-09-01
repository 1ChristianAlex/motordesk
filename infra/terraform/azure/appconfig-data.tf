data "azurerm_app_configuration_key" "ack_database_url" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "DATABASE_URL"
  label                  = var.environment
}

data "azurerm_app_configuration_key" "ack_database_driver" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "DATABASE_DRIVER"
  label                  = var.environment
}

data "azurerm_app_configuration_key" "ack_database_user" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "DATABASE_USER"
  label                  = var.environment
}

data "azurerm_app_configuration_key" "ack_mongo_url" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "MONGO_URL"
  label                  = var.environment
}

data "azurerm_app_configuration_key" "ack_mongo_user" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "MONGO_USER"
  label                  = var.environment
}

data "azurerm_app_configuration_key" "ack_mongo_database" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "MONGO_DATABASE"
  label                  = var.environment
}

data "azurerm_app_configuration_key" "ack_redis_host" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "REDIS_HOST"
  label                  = var.environment
}

data "azurerm_app_configuration_key" "ack_redis_port" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "REDIS_PORT"
  label                  = var.environment
}

data "azurerm_app_configuration_key" "ack_azure_communication_endpoint" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "AZURE_COMMUNICATION_ENDPOINT"
  label                  = var.environment
}

data "azurerm_app_configuration_key" "ack_jwt_issuer" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "JWT_ISSUER"
  label                  = var.environment
}

data "azurerm_app_configuration_key" "ack_jwt_audience" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "JWT_AUDIENCE"
  label                  = var.environment
}

data "azurerm_app_configuration_key" "ack_jwt_realm" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "JWT_REALM"
  label                  = var.environment
}
