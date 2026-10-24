output "api_url" {
  value = module.application.api_url
}

output "certificate_validation_records" {
  value = module.application.certificate_validation_records
}

output "ecr_repository_url" {
  value = module.application.ecr_repository_url
}

output "ecs_cluster_name" {
  value = module.application.ecs_cluster_name
}

output "ecs_service_name" {
  value = module.application.ecs_service_name
}

output "dynamodb_history_table_name" {
  value = module.application.dynamodb_history_table_name
}

output "dynamodb_history_table_arn" {
  value = module.application.dynamodb_history_table_arn
}

output "github_deploy_user_name" {
  value = module.application.github_deploy_user_name
}

output "ses_identity_verification_status" {
  value = module.application.ses_identity_verification_status
}
