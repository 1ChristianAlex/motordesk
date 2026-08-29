resource "random_password" "postgres_admin_password" {
  length  = 16
  special = true
}

resource "azurerm_postgresql_flexible_server" "pg_db" {
  name                          = "${var.environment}-${var.project_name}-psqlflexibleserver"
  resource_group_name           = azurerm_resource_group.rg.name
  location                      = azurerm_resource_group.rg.location
  version                       = var.postgres_version
  delegated_subnet_id           = var.environment == var.environments.prod ? azurerm_subnet.subnet[0].id : null
  private_dns_zone_id           = var.environment == var.environments.prod ? azurerm_private_dns_zone.dns_zone[0].id : null
  public_network_access_enabled = var.environment != var.environments.prod
  administrator_login           = var.postgres_admin_username
  administrator_password        = random_password.postgres_admin_password.result
  zone                          = "1"
  geo_redundant_backup_enabled  = false

  storage_mb   = 32768
  storage_tier = "P4"

  sku_name = "B_Standard_B1ms"
  tags     = var.tags

  depends_on = [
    azurerm_resource_group.rg,
    azurerm_subnet.subnet,
    azurerm_private_dns_zone.dns_zone
  ]
}

resource "azurerm_postgresql_flexible_server_database" "motordesk" {
  name      = var.postgres_database_name
  server_id = azurerm_postgresql_flexible_server.pg_db.id
  charset   = "UTF8"
  collation = "en_US.utf8"
}

resource "azurerm_postgresql_flexible_server_firewall_rule" "dev_postgres_firewall" {
  count            = var.environment == var.environments.prod ? 0 : 1
  name             = "postgres-${var.environment}-${var.project_name}-dev-client"
  server_id        = azurerm_postgresql_flexible_server.pg_db.id
  start_ip_address = var.dev_client_ip
  end_ip_address   = var.dev_client_ip
}


data "azurerm_postgresql_flexible_server" "data_pg" {
  name                = azurerm_postgresql_flexible_server.pg_db.name
  resource_group_name = azurerm_postgresql_flexible_server.pg_db.resource_group_name

  depends_on = [azurerm_postgresql_flexible_server.pg_db]
}
