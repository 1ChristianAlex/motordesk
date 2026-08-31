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
  key                    = "DATABASE_URL"
  label                  = var.environment
  type                   = "kv"
  value                  = "jdbc:postgresql://${data.azurerm_postgresql_flexible_server.data_pg.fqdn}:5432/${azurerm_postgresql_flexible_server_database.motordesk.name}"
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}

resource "azurerm_key_vault_secret" "database_user" {
  name         = "databaseUserv3"
  value        = data.azurerm_postgresql_flexible_server.data_pg.administrator_login
  key_vault_id = azurerm_key_vault.kvault_app.id
}

resource "azurerm_app_configuration_key" "ack_database_user" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "DATABASE_USER"
  label                  = var.environment
  type                   = "vault"
  vault_key_reference    = azurerm_key_vault_secret.database_user.versionless_id
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}

resource "azurerm_key_vault_secret" "database_password" {
  name         = "databasePasswordv3"
  value        = random_password.postgres_admin_password.result
  key_vault_id = azurerm_key_vault.kvault_app.id
}

resource "azurerm_app_configuration_key" "ack_database_password" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "DATABASE_PASSWORD"
  label                  = var.environment
  type                   = "vault"
  vault_key_reference    = azurerm_key_vault_secret.database_password.versionless_id
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}

resource "azurerm_app_configuration_key" "ack_database_driver" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "DATABASE_DRIVER"
  label                  = var.environment
  type                   = "kv"
  value                  = "org.postgresql.Driver"
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}

resource "azurerm_key_vault_secret" "mongo_url" {
  name         = "mongoUrlv3"
  value        = azurerm_mongo_cluster.mongo.connection_strings[0].value
  key_vault_id = azurerm_key_vault.kvault_app.id
}

resource "azurerm_app_configuration_key" "ack_mongo_url" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "MONGO_URL"
  label                  = var.environment
  type                   = "vault"
  vault_key_reference    = azurerm_key_vault_secret.mongo_url.versionless_id
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}

resource "azurerm_key_vault_secret" "mongo_user" {
  name         = "mongoUserv3"
  value        = azurerm_mongo_cluster.mongo.administrator_username
  key_vault_id = azurerm_key_vault.kvault_app.id
}

resource "azurerm_app_configuration_key" "ack_mongo_user" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "MONGO_USER"
  label                  = var.environment
  type                   = "vault"
  vault_key_reference    = azurerm_key_vault_secret.mongo_user.versionless_id
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}

resource "azurerm_key_vault_secret" "mongo_password" {
  name         = "mongoPasswordv3"
  value        = random_password.mongo_admin_password.result
  key_vault_id = azurerm_key_vault.kvault_app.id
}

resource "azurerm_app_configuration_key" "ack_mongo_password" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "MONGO_PASSWORD"
  label                  = var.environment
  type                   = "vault"
  vault_key_reference    = azurerm_key_vault_secret.mongo_password.versionless_id
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}

resource "azurerm_key_vault_secret" "mongo_database" {
  name         = "mongoDatabasev3"
  value        = azurerm_postgresql_flexible_server_database.motordesk.name
  key_vault_id = azurerm_key_vault.kvault_app.id
}

resource "azurerm_app_configuration_key" "ack_mongo_database" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "MONGO_DATABASE"
  label                  = var.environment
  type                   = "vault"
  vault_key_reference    = azurerm_key_vault_secret.mongo_database.versionless_id
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}

resource "azurerm_app_configuration_key" "ack_redis_host" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "REDIS_HOST"
  label                  = var.environment
  type                   = "kv"
  value                  = "redis"
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}

resource "azurerm_app_configuration_key" "ack_redis_port" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "REDIS_PORT"
  label                  = var.environment
  type                   = "kv"
  value                  = "6380"
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}

resource "random_password" "redis_password" {
  length  = 16
  special = true
}

resource "azurerm_key_vault_secret" "redis_password" {
  name         = "redisPasswordv3"
  value        = random_password.redis_password.result
  key_vault_id = azurerm_key_vault.kvault_app.id
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}

resource "azurerm_app_configuration_key" "ack_redis_password" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "REDIS_PASSWORD"
  label                  = var.environment
  type                   = "vault"
  vault_key_reference    = azurerm_key_vault_secret.redis_password.versionless_id
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}

resource "azurerm_key_vault_secret" "azure_communication_access_key" {
  name         = "azureCommunicationAccessKeyv3"
  value        = data.azurerm_communication_service.communication_service.primary_key
  key_vault_id = azurerm_key_vault.kvault_app.id
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}

resource "azurerm_app_configuration_key" "ack_azure_communication_access_key" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "AZURE_COMMUNICATION_ACCESS_KEY"
  label                  = var.environment
  type                   = "vault"
  vault_key_reference    = azurerm_key_vault_secret.azure_communication_access_key.versionless_id
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}

resource "azurerm_key_vault_secret" "azure_communication_endpoint" {
  name         = "azureCommunicationEndpointv3"
  value        = data.azurerm_communication_service.communication_service.hostname
  key_vault_id = azurerm_key_vault.kvault_app.id
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}

resource "azurerm_app_configuration_key" "ack_azure_communication_endpoint" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "AZURE_COMMUNICATION_ENDPOINT"
  label                  = var.environment
  type                   = "vault"
  vault_key_reference    = azurerm_key_vault_secret.azure_communication_endpoint.versionless_id
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}
resource "random_string" "ack_jwt_issuer" {
  length  = 16
  special = true
}
resource "azurerm_app_configuration_key" "ack_jwt_issuer" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "JWT_ISSUER"
  label                  = var.environment
  type                   = "kv"
  value                  = random_string.ack_jwt_issuer.result
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}

resource "random_string" "ack_jwt_audience" {
  length  = 16
  special = true
}

resource "azurerm_app_configuration_key" "ack_jwt_audience" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "JWT_AUDIENCE"
  label                  = var.environment
  type                   = "kv"
  value                  = random_string.ack_jwt_audience.result
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}

resource "random_string" "ack_jwt_realm" {
  length  = 16
  special = true
}

resource "azurerm_app_configuration_key" "ack_jwt_realm" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "JWT_REALM"
  label                  = var.environment
  type                   = "kv"
  value                  = random_string.ack_jwt_realm.result
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}

resource "random_password" "jwt_secret" {
  length  = 16
  special = true
}

resource "azurerm_key_vault_secret" "jwt_secret" {
  name         = "jwtSecretv3"
  value        = random_password.jwt_secret.result
  key_vault_id = azurerm_key_vault.kvault_app.id
}

resource "azurerm_app_configuration_key" "ack_jwt_secret" {
  configuration_store_id = azurerm_app_configuration.app_conf.id
  key                    = "JWT_SECRET"
  label                  = var.environment
  type                   = "vault"
  vault_key_reference    = azurerm_key_vault_secret.jwt_secret.versionless_id
  depends_on = [
    azurerm_role_assignment.appconf_dataowner
  ]
}

# resource "azurerm_key_vault_secret" "aks_client_certificate" {
#   name         = "aksClientCertificatev3"
#   value        = azurerm_kubernetes_cluster.aks.kube_config[0].client_certificate
#   key_vault_id = azurerm_key_vault.kvault_app.id
# }

# resource "azurerm_app_configuration_key" "ack_aks_client_certificate" {
#   configuration_store_id = azurerm_app_configuration.app_conf.id
#   key                    = "AKS_CLIENT_CERTIFICATE"
#   label                  = var.environment
#   type                   = "vault"
#   vault_key_reference    = azurerm_key_vault_secret.aks_client_certificate.versionless_id
#   depends_on = [
#     azurerm_role_assignment.appconf_dataowner
#   ]
# }

#
# resource "azurerm_key_vault_secret" "kube_config" {
#   name         = "aksClientKubeConfigRawv3"
#   value        = azurerm_kubernetes_cluster.aks.kube_config_raw
#   key_vault_id = azurerm_key_vault.kvault_app.id
# }
#
# resource "azurerm_app_configuration_key" "ack_kube_config" {
#   configuration_store_id = azurerm_app_configuration.app_conf.id
#   key                    = "AKS_CLIENT_KUBE_CONFIG_RAW"
#   label                  = var.environment
#   type                   = "vault"
#   vault_key_reference    = azurerm_key_vault_secret.kube_config.versionless_id
#   depends_on = [
#     azurerm_role_assignment.appconf_dataowner
#   ]
# }
#

