
# NOTE: the Name used for Redis needs to be globally unique
resource "azurerm_redis_cache" "redis" {
  name                 = "${var.environment}-${var.project_name}-redis"
  location             = azurerm_resource_group.rg.location
  resource_group_name  = azurerm_resource_group.rg.name
  capacity             = 2
  family               = "C"
  sku_name             = "Standard"
  non_ssl_port_enabled = false
  minimum_tls_version  = "1.2"

  redis_configuration {
    maxmemory_reserved = 2
    maxmemory_delta    = 2
    maxmemory_policy   = "allkeys-lru"
  }
}


resource "azurerm_redis_firewall_rule" "dev_redis_firewall" {
  redis_cache_name    = azurerm_redis_cache.redis.name
  resource_group_name = azurerm_resource_group.rg.name

  count = var.environment == "prod" ? 0 : 1
  name  = "redis_${var.environment}_${var.project_name}_dev_client"

  start_ip = var.dev_client_ip
  end_ip   = var.dev_client_ip
}
