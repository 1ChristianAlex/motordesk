data "azurerm_key_vault_secret" "database_user" {
  name         = azurerm_key_vault_secret.database_user.name
  key_vault_id = azurerm_key_vault.kvault_app.id
  depends_on   = [azurerm_key_vault.kvault_app, azurerm_key_vault_secret.database_user]
}

data "azurerm_key_vault_secret" "database_password" {
  name         = azurerm_key_vault_secret.database_password.name
  key_vault_id = azurerm_key_vault.kvault_app.id
  depends_on   = [azurerm_key_vault.kvault_app, azurerm_key_vault_secret.database_password]
}

data "azurerm_key_vault_secret" "mongo_url" {
  name         = azurerm_key_vault_secret.mongo_url.name
  key_vault_id = azurerm_key_vault.kvault_app.id
  depends_on   = [azurerm_key_vault.kvault_app, azurerm_key_vault_secret.mongo_url]
}

data "azurerm_key_vault_secret" "mongo_user" {
  name         = azurerm_key_vault_secret.mongo_user.name
  key_vault_id = azurerm_key_vault.kvault_app.id
  depends_on   = [azurerm_key_vault.kvault_app, azurerm_key_vault_secret.mongo_user]
}

data "azurerm_key_vault_secret" "mongo_password" {
  name         = azurerm_key_vault_secret.mongo_password.name
  key_vault_id = azurerm_key_vault.kvault_app.id
  depends_on   = [azurerm_key_vault.kvault_app, azurerm_key_vault_secret.mongo_password]
}

data "azurerm_key_vault_secret" "mongo_database" {
  name         = azurerm_key_vault_secret.mongo_database.name
  key_vault_id = azurerm_key_vault.kvault_app.id
  depends_on   = [azurerm_key_vault.kvault_app, azurerm_key_vault_secret.mongo_database]
}

data "azurerm_key_vault_secret" "redis_password" {
  name         = azurerm_key_vault_secret.redis_password.name
  key_vault_id = azurerm_key_vault.kvault_app.id
  depends_on   = [azurerm_key_vault.kvault_app, azurerm_key_vault_secret.redis_password]
}

data "azurerm_key_vault_secret" "azure_communication_access_key" {
  name         = azurerm_key_vault_secret.azure_communication_access_key.name
  key_vault_id = azurerm_key_vault.kvault_app.id
  depends_on   = [azurerm_key_vault.kvault_app, azurerm_key_vault_secret.azure_communication_access_key]
}

data "azurerm_key_vault_secret" "azure_communication_endpoint" {
  name         = azurerm_key_vault_secret.azure_communication_endpoint.name
  key_vault_id = azurerm_key_vault.kvault_app.id
  depends_on   = [azurerm_key_vault.kvault_app, azurerm_key_vault_secret.azure_communication_endpoint]
}

data "azurerm_key_vault_secret" "jwt_secret" {
  name         = azurerm_key_vault_secret.jwt_secret.name
  key_vault_id = azurerm_key_vault.kvault_app.id
  depends_on   = [azurerm_key_vault.kvault_app, azurerm_key_vault_secret.jwt_secret]
}
