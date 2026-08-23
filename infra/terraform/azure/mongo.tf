resource "azurerm_mongo_cluster" "mongo" {
  name                = "${var.environment}-${var.project_name}-mongo-mc"
  resource_group_name = azurerm_resource_group.rg.name
  location            = azurerm_resource_group.rg.location

  administrator_username = var.mongo_admin_username
  administrator_password = var.mongo_admin_username
  compute_tier           = "Free"
  high_availability_mode = "Disabled"
  shard_count            = "1"
  storage_size_in_gb     = "32"
  version                = "8.0"
}

resource "azurerm_mongo_cluster_firewall_rule" "example" {
  count = var.environment == "prod" ? 0 : 1
  name  = "mongo-${var.environment}-${var.project_name}-dev-client"

  mongo_cluster_id = azurerm_mongo_cluster.mongo.id
  start_ip_address = var.dev_client_ip
  end_ip_address   = var.dev_client_ip
}

