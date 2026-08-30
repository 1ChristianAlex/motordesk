resource "azurerm_log_analytics_workspace" "container_apps" {
  name                = "logs-${var.environment}-${var.project_name}"
  location            = azurerm_resource_group.rg.location
  resource_group_name = azurerm_resource_group.rg.name

  sku               = "PerGB2018"
  retention_in_days = 30

  tags = var.tags
}

resource "azurerm_container_app_environment" "main" {
  name                       = "cae-${var.environment}-${var.project_name}"
  location                   = azurerm_resource_group.rg.location
  resource_group_name        = azurerm_resource_group.rg.name
  log_analytics_workspace_id = azurerm_log_analytics_workspace.container_apps.id
  infrastructure_subnet_id   = azurerm_subnet.container_apps.id
  logs_destination           = "log-analytics"

  tags = var.tags
}

resource "azurerm_user_assigned_identity" "api" {
  name                = "mi-${var.environment}-${var.project_name}-api"
  resource_group_name = azurerm_resource_group.rg.name
  location            = azurerm_resource_group.rg.location

  tags = var.tags
}

resource "azurerm_user_assigned_identity" "redis" {
  name                = "mi-${var.environment}-${var.project_name}-redis"
  resource_group_name = azurerm_resource_group.rg.name
  location            = azurerm_resource_group.rg.location

  tags = var.tags
}

resource "azurerm_role_assignment" "api_acr_pull" {
  scope                = azurerm_container_registry.acr.id
  role_definition_name = "AcrPull"
  principal_id         = azurerm_user_assigned_identity.api.principal_id
}

resource "azurerm_role_assignment" "redis_acr_pull" {
  scope                = azurerm_container_registry.acr.id
  role_definition_name = "AcrPull"
  principal_id         = azurerm_user_assigned_identity.redis.principal_id
}

resource "azurerm_container_app" "main_api" {
  count = var.image_tag == "" ? 0 : 1

  name                         = "main-api-${var.environment}-${var.project_name}"
  container_app_environment_id = azurerm_container_app_environment.main.id
  resource_group_name          = azurerm_resource_group.rg.name

  revision_mode = "Single"

  identity {
    type         = "UserAssigned"
    identity_ids = [azurerm_user_assigned_identity.api.id]
  }

  registry {
    server   = azurerm_container_registry.acr.login_server
    identity = azurerm_user_assigned_identity.api.id
  }

  template {
    min_replicas = 1
    max_replicas = 1

    container {
      name   = "${var.project_name}-api"
      image  = "${azurerm_container_registry.acr.login_server}/${var.project_name}/api:${var.image_tag}"
      cpu    = 0.5
      memory = "1Gi"

      env {
        name  = "HTTP_HOST"
        value = "0.0.0.0"
      }

      env {
        name  = "HTTP_PORT"
        value = "8080"
      }

      env {
        name  = "HTTP_PROTOCOL"
        value = "http"
      }

      # PostgreSQL
      env {
        name  = "DATABASE_URL"
        value = azurerm_app_configuration_key.ack_database_url.value
      }

      env {
        name  = "DATABASE_DRIVER"
        value = azurerm_app_configuration_key.ack_database_driver.value
      }

      env {
        name  = "DATABASE_USER"
        value = azurerm_app_configuration_key.ack_database_user.value
      }

      env {
        name        = "DATABASE_PASSWORD"
        secret_name = "database-password"
      }

      # Mongo
      env {
        name  = "MONGO_URL"
        value = azurerm_app_configuration_key.ack_mongo_url.value
      }

      env {
        name  = "MONGO_USER"
        value = azurerm_app_configuration_key.ack_mongo_user.value
      }

      env {
        name        = "MONGO_PASSWORD"
        secret_name = "mongo-password"
      }

      env {
        name  = "MONGO_DATABASE"
        value = azurerm_app_configuration_key.ack_mongo_database.value
      }

      # Redis
      env {
        name  = "REDIS_HOST"
        value = azurerm_app_configuration_key.ack_redis_host.value
      }

      env {
        name  = "REDIS_PORT"
        value = azurerm_app_configuration_key.ack_redis_port.value
      }

      env {
        name        = "REDIS_PASSWORD"
        secret_name = "redis-password"
      }

      # Azure Communication Services
      env {
        name  = "AZURE_COMMUNICATION_ENDPOINT"
        value = azurerm_app_configuration_key.ack_azure_communication_endpoint.value
      }

      env {
        name        = "AZURE_COMMUNICATION_ACCESS_KEY"
        secret_name = "azure-communication-access-key"
      }

      # JWT
      env {
        name  = "JWT_ISSUER"
        value = azurerm_app_configuration_key.ack_jwt_issuer.value
      }

      env {
        name  = "JWT_AUDIENCE"
        value = azurerm_app_configuration_key.ack_jwt_audience.value
      }

      env {
        name  = "JWT_REALM"
        value = azurerm_app_configuration_key.ack_jwt_realm.value
      }

      env {
        name        = "JWT_SECRET"
        secret_name = "jwt-secret"
      }
    }
  }

  secret {
    name  = "database-password"
    value = azurerm_key_vault_secret.database_password.value
  }

  secret {
    name  = "mongo-password"
    value = azurerm_key_vault_secret.mongo_password.value
  }

  secret {
    name  = "redis-password"
    value = azurerm_key_vault_secret.redis_password.value
  }

  secret {
    name  = "azure-communication-access-key"
    value = azurerm_key_vault_secret.azure_communication_access_key.value
  }

  secret {
    name  = "jwt-secret"
    value = azurerm_key_vault_secret.jwt_secret.value
  }

  ingress {
    external_enabled           = true
    allow_insecure_connections = false
    target_port                = 8080
    transport                  = "http"

    traffic_weight {
      percentage      = 100
      latest_revision = true
    }
  }

  tags = var.tags
}

resource "azurerm_container_app" "main_redis" {
  count = var.image_tag == "" ? 0 : 1

  name                         = "main-redis-${var.environment}-${var.project_name}"
  container_app_environment_id = azurerm_container_app_environment.main.id
  resource_group_name          = azurerm_resource_group.rg.name

  revision_mode = "Single"

  identity {
    type         = "UserAssigned"
    identity_ids = [azurerm_user_assigned_identity.redis.id]
  }

  registry {
    server   = azurerm_container_registry.acr.login_server
    identity = azurerm_user_assigned_identity.redis.id
  }

  template {
    min_replicas = 1
    max_replicas = 1

    container {
      name   = "${var.project_name}-redis"
      image  = "${azurerm_container_registry.acr.login_server}/${var.project_name}/redis:${var.image_tag}"
      cpu    = 0.25
      memory = "0.5Gi"

      env {
        name  = "REDIS_HOST"
        value = azurerm_app_configuration_key.ack_redis_host.value
      }

      env {
        name  = "REDIS_PORT"
        value = azurerm_app_configuration_key.ack_redis_port.value
      }

      env {
        name        = "REDIS_PASSWORD"
        secret_name = "redis-password"
      }
    }
  }

  secret {
    name  = "redis-password"
    value = azurerm_key_vault_secret.redis_password.value
  }

  ingress {
    external_enabled = false
    target_port      = azurerm_app_configuration_key.ack_redis_port.value
    transport        = "tcp"

    traffic_weight {
      percentage      = 100
      latest_revision = true
    }
  }

  tags = var.tags
}
