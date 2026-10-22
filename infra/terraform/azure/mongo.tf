resource "random_password" "mongo_admin_password" {
  length  = 16
  special = true
}

resource "azurerm_mongo_cluster" "mongo" {
  name                = "${var.environment}-${var.project_name}-mongo-mc"
  resource_group_name = azurerm_resource_group.rg.name
  location            = azurerm_resource_group.rg.location

  administrator_username = var.mongo_admin_username
  administrator_password = random_password.mongo_admin_password.result
  compute_tier           = "Free"
  high_availability_mode = "Disabled"
  shard_count            = "1"
  storage_size_in_gb     = "32"
  version                = "8.0"
}

resource "azurerm_mongo_cluster_firewall_rule" "dev_mongo_firewall" {
  # dev/qa: allow all IPs because Container Apps egress IP is not static on the student subscription.
  # prod: restrict to known egress IPs before go-live (replace this rule).
  count = var.mongo_firewall_allow_all && var.environment != var.environments.prod ? 1 : 0
  name  = "mongo-${var.environment}-${var.project_name}-apps"

  mongo_cluster_id = azurerm_mongo_cluster.mongo.id
  start_ip_address = "0.0.0.0"
  end_ip_address   = "255.255.255.255"
}
