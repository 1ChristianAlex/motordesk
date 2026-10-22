locals {
  tags = merge(var.tags, {
    environment = var.environment
  })

  redis_internal_host = var.image_tag == "" ? null : "${azurerm_container_app.main_redis[0].name}.${azurerm_container_app_environment.main.default_domain}"
}
