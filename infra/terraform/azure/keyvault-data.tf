data "azurerm_key_vault_secret" "database_user" {
  name         = "databaseUserv3"
  key_vault_id = data.azurerm_key_vault.kvault_app.id
  depends_on   = [azurerm_key_vault_access_policy.terraform]
}

data "azurerm_key_vault_secret" "database_password" {
  name         = "databasePasswordv3"
  key_vault_id = data.azurerm_key_vault.kvault_app.id
  depends_on   = [azurerm_key_vault_access_policy.terraform]
}

data "azurerm_key_vault_secret" "mongo_url" {
  name         = "mongoUrlv3"
  key_vault_id = data.azurerm_key_vault.kvault_app.id
  depends_on   = [azurerm_key_vault_access_policy.terraform]
}

data "azurerm_key_vault_secret" "mongo_user" {
  name         = "mongoUserv3"
  key_vault_id = data.azurerm_key_vault.kvault_app.id
  depends_on   = [azurerm_key_vault_access_policy.terraform]
}

data "azurerm_key_vault_secret" "mongo_password" {
  name         = "mongoPasswordv3"
  key_vault_id = data.azurerm_key_vault.kvault_app.id
  depends_on   = [azurerm_key_vault_access_policy.terraform]
}

data "azurerm_key_vault_secret" "mongo_database" {
  name         = "mongoDatabasev3"
  key_vault_id = data.azurerm_key_vault.kvault_app.id
  depends_on   = [azurerm_key_vault_access_policy.terraform]
}

data "azurerm_key_vault_secret" "redis_password" {
  name         = "redisPasswordv3"
  key_vault_id = data.azurerm_key_vault.kvault_app.id
  depends_on   = [azurerm_key_vault_access_policy.terraform]
}

data "azurerm_key_vault_secret" "azure_communication_access_key" {
  name         = "azureCommunicationAccessKeyv3"
  key_vault_id = data.azurerm_key_vault.kvault_app.id
  depends_on   = [azurerm_key_vault_access_policy.terraform]
}

data "azurerm_key_vault_secret" "azure_communication_endpoint" {
  name         = "azureCommunicationEndpointv3"
  key_vault_id = data.azurerm_key_vault.kvault_app.id
  depends_on   = [azurerm_key_vault_access_policy.terraform]
}

data "azurerm_key_vault_secret" "jwt_secret" {
  name         = "jwtSecretv3"
  key_vault_id = data.azurerm_key_vault.kvault_app.id
  depends_on   = [azurerm_key_vault_access_policy.terraform]
}
