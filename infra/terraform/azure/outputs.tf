output "acr_login_server" {
  description = "Azure Container Registry login server."
  value       = azurerm_container_registry.acr.login_server
}

output "acr_id" {
  description = "Azure Container Registry resource ID."
  value       = azurerm_container_registry.acr.id
}

resource "azurerm_app_configuration_key" "ack_database_url" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "databaseUrl"
  type                   = "kv"
  value                  = "jdbc:postgresql://${data.azurerm_postgresql_flexible_server.data_pg.fqdn}:5432/${azurerm_postgresql_flexible_server_database.motordesk.name}"
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}

resource "azurerm_key_vault_secret" "database_user" {
  name         = "databaseUser"
  value        = data.azurerm_postgresql_flexible_server.data_pg.administrator_login
  key_vault_id = azurerm_key_vault.key_vault_app.id
}

resource "azurerm_app_configuration_key" "ack_database_user" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "databaseUser"
  type                   = "vault"
  vault_key_reference    = azurerm_key_vault_secret.database_user.versionless_id
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}

resource "azurerm_key_vault_secret" "database_password" {
  name         = "databasePassword"
  value        = var.postgres_admin_password
  key_vault_id = azurerm_key_vault.key_vault_app.id
}

resource "azurerm_app_configuration_key" "ack_database_password" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "databasePassword"
  type                   = "vault"
  vault_key_reference    = azurerm_key_vault_secret.database_password.versionless_id
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}

resource "azurerm_app_configuration_key" "ack_database_driver" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "databaseDriver"
  type                   = "kv"
  value                  = "org.postgresql.Driver"
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}

resource "azurerm_key_vault_secret" "mongo_url" {
  name         = "mongoUrl"
  value        = azurerm_mongo_cluster.mongo.connection_strings[0].value
  key_vault_id = azurerm_key_vault.key_vault_app.id
}

resource "azurerm_app_configuration_key" "ack_mongo_url" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "mongoUrl"
  type                   = "vault"
  vault_key_reference    = azurerm_key_vault_secret.mongo_url.versionless_id
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}

resource "azurerm_key_vault_secret" "mongo_user" {
  name         = "mongoUser"
  value        = azurerm_mongo_cluster.mongo.administrator_username
  key_vault_id = azurerm_key_vault.key_vault_app.id
}

resource "azurerm_app_configuration_key" "ack_mongo_user" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "mongoUser"
  type                   = "vault"
  vault_key_reference    = azurerm_key_vault_secret.mongo_user.versionless_id
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}

resource "azurerm_key_vault_secret" "mongo_password" {
  name         = "mongoPassword"
  value        = var.mongo_admin_password
  key_vault_id = azurerm_key_vault.key_vault_app.id
}

resource "azurerm_app_configuration_key" "ack_mongo_password" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "mongoPassword"
  type                   = "vault"
  vault_key_reference    = azurerm_key_vault_secret.mongo_password.versionless_id
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}

resource "azurerm_key_vault_secret" "mongo_database" {
  name         = "mongoDatabase"
  value        = azurerm_postgresql_flexible_server_database.motordesk.name
  key_vault_id = azurerm_key_vault.key_vault_app.id
}

resource "azurerm_app_configuration_key" "ack_mongo_database" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "mongoDatabase"
  type                   = "vault"
  vault_key_reference    = azurerm_key_vault_secret.mongo_database.versionless_id
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}

resource "azurerm_app_configuration_key" "ack_redis_host" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "redisHost"
  type                   = "kv"
  value                  = azurerm_redis_cache.redis.hostname
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}

resource "azurerm_app_configuration_key" "ack_redis_port" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "redisPort"
  type                   = "kv"
  value                  = "6380"
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}

resource "azurerm_key_vault_secret" "redis_password" {
  name         = "redisPassword"
  value        = azurerm_redis_cache.redis.primary_access_key
  key_vault_id = azurerm_key_vault.key_vault_app.id
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}

resource "azurerm_app_configuration_key" "ack_redis_password" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "redisPassword"
  type                   = "vault"
  vault_key_reference    = azurerm_key_vault_secret.redis_password.versionless_id
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}

resource "azurerm_key_vault_secret" "azure_communication_access_key" {
  name         = "azureCommunicationAccessKey"
  value        = data.azurerm_communication_service.communication_service.primary_key
  key_vault_id = azurerm_key_vault.key_vault_app.id
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}

resource "azurerm_app_configuration_key" "ack_azure_communication_access_key" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "azureCommunicationAccessKey"
  type                   = "vault"
  vault_key_reference    = azurerm_key_vault_secret.azure_communication_access_key.versionless_id
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}

resource "azurerm_key_vault_secret" "azure_communication_endpoint" {
  name         = "azureCommunicationEndpoint"
  value        = data.azurerm_communication_service.communication_service.hostname
  key_vault_id = azurerm_key_vault.key_vault_app.id
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}

resource "azurerm_app_configuration_key" "ack_azure_communication_endpoint" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "azureCommunicationEndpoint"
  type                   = "vault"
  vault_key_reference    = azurerm_key_vault_secret.azure_communication_endpoint.versionless_id
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}

resource "azurerm_app_configuration_key" "ack_jwt_issuer" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "jwtIssuer"
  type                   = "kv"
  value                  = "ktor-api"
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}

resource "azurerm_app_configuration_key" "ack_jwt_audience" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "jwtAudience"
  type                   = "kv"
  value                  = "ktor-users"
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}

resource "azurerm_app_configuration_key" "ack_jwt_realm" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "jwtRealm"
  type                   = "kv"
  value                  = "ktor-app"
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}

resource "azurerm_key_vault_secret" "jwt_secret" {
  name         = "jwtSecret"
  value        = "super-secret-key"
  key_vault_id = azurerm_key_vault.key_vault_app.id
}

resource "azurerm_app_configuration_key" "ack_jwt_secret" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "jwtSecret"
  type                   = "vault"
  vault_key_reference    = azurerm_key_vault_secret.jwt_secret.versionless_id
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}
