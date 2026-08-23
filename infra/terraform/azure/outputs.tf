output "postgres_host" {
  description = "PostgreSQL Flexible Server hostname."
  value       = azurerm_postgresql_flexible_server.pg_db.fqdn
}

output "postgres_url" {
  description = "JDBC URL for PostgreSQL."
  value       = "jdbc:postgresql://${azurerm_postgresql_flexible_server.pg_db.fqdn}:5432/${var.postgres_database_name}"
}

output "postgres_user" {
  description = "PostgreSQL administrator username."
  value       = var.postgres_admin_username
  sensitive   = true
}

output "postgres_password" {
  description = "PostgreSQL administrator password."
  value       = var.postgres_admin_password
  sensitive   = true
}

output "postgres_driver" {
  description = "PostgreSQL JDBC driver class."
  value       = "org.postgresql.Driver"
}

output "mongo_connection_string" {
  description = "Mongo cluster connection string."
  value       = azurerm_mongo_cluster.mongo.connection_strings[0].value
  sensitive   = true
}

output "mongo_database" {
  description = "Mongo database name."
  value       = var.postgres_database_name
}

output "mongo_user" {
  description = "Mongo administrator username."
  value       = var.mongo_admin_username
  sensitive   = true
}

output "mongo_password" {
  description = "Mongo administrator password."
  value       = var.mongo_admin_password
  sensitive   = true
}

output "redis_host" {
  description = "Redis cache hostname."
  value       = azurerm_redis_cache.redis.hostname
}

output "redis_port" {
  description = "Redis TLS port."
  value       = 6380
}

output "redis_password" {
  description = "Redis cache primary access key."
  value       = azurerm_redis_cache.redis.primary_access_key
  sensitive   = true
}

output "acr_login_server" {
  description = "Azure Container Registry login server."
  value       = azurerm_container_registry.acr.login_server
}

output "acr_id" {
  description = "Azure Container Registry resource ID."
  value       = azurerm_container_registry.acr.id
}

output "access_key" {
  description = "Azure user access key"
  value = azurerm_role_assignment.current_user.condition
}