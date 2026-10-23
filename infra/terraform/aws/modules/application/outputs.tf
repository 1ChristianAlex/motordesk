output "api_url" {
  value = "https://${var.api_domain_name}"
}

output "ecr_repository_url" {
  value = aws_ecr_repository.api.repository_url
}

output "ecs_cluster_name" {
  value = aws_ecs_cluster.main.name
}

output "ecs_service_name" {
  value = aws_ecs_service.api.name
}

output "github_deploy_role_arn" {
  value = aws_iam_role.github_deploy.arn
}

output "postgres_endpoint" {
  value = aws_db_instance.postgres.address
}

output "documentdb_endpoint" {
  value = aws_docdb_cluster.main.endpoint
}

output "redis_endpoint" {
  value = aws_elasticache_replication_group.redis.primary_endpoint_address
}

output "ses_identity_verification_status" {
  value = aws_sesv2_email_identity.sender.verified_for_sending_status
}
