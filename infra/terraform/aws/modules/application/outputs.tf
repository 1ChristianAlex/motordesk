output "api_url" {
  value = var.api_domain_name == "" ? aws_apigatewayv2_api.api.api_endpoint : "https://${var.api_domain_name}"
}

output "certificate_validation_records" {
  value = var.api_domain_name == "" ? {} : {
    for option in aws_acm_certificate.api[0].domain_validation_options :
    option.domain_name => {
      name  = option.resource_record_name
      type  = option.resource_record_type
      value = option.resource_record_value
    }
  }
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

output "github_deploy_user_name" {
  value = aws_iam_user.github_deploy.name
}

output "postgres_endpoint" {
  value = aws_db_instance.postgres.address
}

output "dynamodb_history_table_name" {
  value = aws_dynamodb_table.history.name
}

output "dynamodb_history_table_arn" {
  value = aws_dynamodb_table.history.arn
}

output "redis_endpoint" {
  value = aws_elasticache_replication_group.redis.primary_endpoint_address
}

output "ses_identity_verification_status" {
  value = aws_sesv2_email_identity.sender.verified_for_sending_status
}
