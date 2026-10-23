output "api_url" {
  value = module.application.api_url
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

output "github_deploy_role_arn" {
  value = module.application.github_deploy_role_arn
}

output "ses_identity_verification_status" {
  value = module.application.ses_identity_verification_status
}
