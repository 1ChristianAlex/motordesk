
resource "azurerm_subnet" "subnet_db" {
  count                = var.environment == "prod" ? 1 : 0
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
}

resource "azurerm_private_dns_zone" "dns_zone_db" {
  count               = var.environment == "prod" ? 1 : 0
  name                = "${var.environment}-${var.project_name}.postgres.database.azure.com"
  resource_group_name = azurerm_resource_group.rg.name
  tags                = var.tags
}

resource "azurerm_private_dns_zone_virtual_network_link" "dns_zone_virtual_network_link_db" {
  count               = var.environment == "prod" ? 1 : 0
  name                = "${var.environment}-${var.project_name}VnetZone.com"
  private_dns_zone_id = azurerm_private_dns_zone.dns_zone_db[0].id
  tags                = var.tags
  virtual_network_id  = azurerm_virtual_network.network.id
}

resource "azurerm_postgresql_flexible_server" "pg_db" {
  name                          = "${var.environment}-${var.project_name}-psqlflexibleserver"
  resource_group_name           = azurerm_resource_group.rg.name
  location                      = azurerm_resource_group.rg.location
  version                       = var.postgres_version
  delegated_subnet_id           = var.environment == "prod" ? azurerm_subnet.subnet_db[0].id : null
  private_dns_zone_id           = var.environment == "prod" ? azurerm_private_dns_zone.dns_zone_db[0].id : null
  public_network_access_enabled = var.environment != "prod"
  administrator_login           = var.postgres_admin_username
  administrator_password        = var.postgres_admin_password
  zone                          = "1"
  geo_redundant_backup_enabled  = false

  storage_mb   = 32768
  storage_tier = "P4"

  sku_name = "B_Standard_B1ms"
  tags     = var.tags
}

resource "azurerm_postgresql_flexible_server_database" "motordesk" {
  name      = var.postgres_database_name
  server_id = azurerm_postgresql_flexible_server.pg_db.id
  charset   = "UTF8"
  collation = "en_US.utf8"
}

resource "azurerm_postgresql_flexible_server_firewall_rule" "dev_postgres_firewall" {
  count            = var.environment == "prod" ? 0 : 1
  name             = "postgres-${var.environment}-${var.project_name}-dev-client"
  server_id        = azurerm_postgresql_flexible_server.pg_db.id
  start_ip_address = var.dev_client_ip
  end_ip_address   = var.dev_client_ip
}
