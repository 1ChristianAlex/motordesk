
resource "azurerm_subnet" "subnet_db" {
  name                 = "${var.environment}-${var.project_name}-subnet-db"
  resource_group_name  = azurerm_resource_group.rg.name
  virtual_network_name = azurerm_virtual_network.network.name
  address_prefixes     = ["10.20.0.0/24"]

  delegation {
    name = "fs"
    service_delegation {
      name = "Microsoft.DBforPostgreSQL/flexibleServers"
      actions = [
        "Microsoft.Network/virtualNetworks/subnets/join/action",
      ]
    }
  }
  depends_on = [azurerm_resource_group.rg, azurerm_virtual_network.network]
}

resource "azurerm_private_dns_zone" "dns_zone_db" {
  name                = "${var.environment}-${var.project_name}.postgres.database.azure.com"
  resource_group_name = azurerm_resource_group.rg.name
  tags                = var.tags

  depends_on = [azurerm_resource_group.rg]

}

resource "azurerm_private_dns_zone_virtual_network_link" "dns_zone_virtual_network_link_db" {
  name                = "${var.environment}-${var.project_name}VnetZone.com"
  private_dns_zone_id = azurerm_private_dns_zone.dns_zone_db.id
  tags                = var.tags
  virtual_network_id  = azurerm_virtual_network.network.id
  depends_on          = [azurerm_subnet.subnet_db]
}

resource "azurerm_postgresql_flexible_server" "pg_db" {
  name                          = "${var.environment}-${var.project_name}-psqlflexibleserver"
  resource_group_name           = azurerm_resource_group.rg.name
  location                      = azurerm_resource_group.rg.location
  version                       = var.postgres_version
  delegated_subnet_id           = azurerm_subnet.subnet_db.id
  private_dns_zone_id           = azurerm_private_dns_zone.dns_zone_db.id
  public_network_access_enabled = true
  administrator_login           = var.postgres_admin_username
  administrator_password        = var.postgres_admin_password
  zone                          = "1"
  geo_redundant_backup_enabled  = false

  storage_mb   = 32768
  storage_tier = "P4"

  sku_name   = "B_Standard_B1ms"
  depends_on = [azurerm_private_dns_zone_virtual_network_link.dns_zone_virtual_network_link_db]
  tags       = var.tags
}

resource "azurerm_postgresql_flexible_server_database" "motordesk" {
  name      = var.postgres_database_name
  server_id = azurerm_postgresql_flexible_server.pg_db.id
  charset   = "UTF8"
  collation = "en_US.utf8"
}
