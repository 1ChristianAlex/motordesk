output "acr_login_server" {
  description = "Azure Container Registry login server."
  value       = azurerm_container_registry.acr.login_server
}

output "acr_id" {
  description = "Azure Container Registry resource ID."
  value       = azurerm_container_registry.acr.id
}

resource "azurerm_key_vault_secret" "database_url" {
  name         = "databaseUrl"
  value        = "jdbc:postgresql://${azurerm_postgresql_flexible_server.pg_db.fqdn}:5432/${var.postgres_database_name}"
  key_vault_id = azurerm_key_vault.key_vault_app.id
}

resource "azurerm_key_vault_secret" "database_user" {
  name         = "databaseUser"
  value        = var.postgres_admin_username
  key_vault_id = azurerm_key_vault.key_vault_app.id
}

resource "azurerm_key_vault_secret" "database_password" {
  name         = "databasePassword"
  value        = var.postgres_admin_password
  key_vault_id = azurerm_key_vault.key_vault_app.id
}

resource "azurerm_key_vault_secret" "database_driver" {
  name         = "databaseDriver"
  value        = "org.postgresql.Driver"
  key_vault_id = azurerm_key_vault.key_vault_app.id
}

resource "azurerm_key_vault_secret" "mongo_url" {
  name         = "mongoUrl"
  value        = azurerm_mongo_cluster.mongo.connection_strings[0].value
  key_vault_id = azurerm_key_vault.key_vault_app.id
}

resource "azurerm_key_vault_secret" "mongo_user" {
  name         = "mongoUser"
  value        = var.mongo_admin_username
  key_vault_id = azurerm_key_vault.key_vault_app.id
}

resource "azurerm_key_vault_secret" "mongo_password" {
  name         = "mongoPassword"
  value        = var.mongo_admin_password
  key_vault_id = azurerm_key_vault.key_vault_app.id
}

resource "azurerm_key_vault_secret" "mongo_database" {
  name         = "mongoDatabase"
  value        = var.postgres_database_name
  key_vault_id = azurerm_key_vault.key_vault_app.id
}

resource "azurerm_key_vault_secret" "redis_host" {
  name         = "redisHost"
  value        = azurerm_redis_cache.redis.hostname
  key_vault_id = azurerm_key_vault.key_vault_app.id
}

resource "azurerm_key_vault_secret" "redis_port" {
  name         = "redisPort"
  value        = 6380
  key_vault_id = azurerm_key_vault.key_vault_app.id
}

resource "azurerm_key_vault_secret" "redis_password" {
  name         = "redisPassword"
  value        = azurerm_redis_cache.redis.primary_access_key
  key_vault_id = azurerm_key_vault.key_vault_app.id
}

resource "azurerm_key_vault_secret" "azure_communication_access_key" {
  name         = "azureCommunicationAccessKey"
  value        = jsondecode(azapi_resource.communicationService.output).properties.api
  key_vault_id = azurerm_key_vault.key_vault_app.id
}

resource "azurerm_key_vault_secret" "azure_communication_endpoint" {
  name         = "azureCommunicationEndpoint"
  value        = jsondecode(azapi_resource.communicationService.output).properties.hostName
  key_vault_id = azurerm_key_vault.key_vault_app.id
}

resource "azurerm_key_vault_secret" "jwt_issuer" {
  name         = "jwtIssuer"
  value        = "ktor-api"
  key_vault_id = azurerm_key_vault.key_vault_app.id
}

resource "azurerm_key_vault_secret" "jwt_audience" {
  name         = "jwtAudience"
  value        = "ktor-users"
  key_vault_id = azurerm_key_vault.key_vault_app.id
}

resource "azurerm_key_vault_secret" "jwt_realm" {
  name         = "jwtRealm"
  value        = "ktor-app"
  key_vault_id = azurerm_key_vault.key_vault_app.id
}

resource "azurerm_key_vault_secret" "jwt_secret" {
  name         = "jwtSecret"
  value        = "super-secret-key"
  key_vault_id = azurerm_key_vault.key_vault_app.id
}
