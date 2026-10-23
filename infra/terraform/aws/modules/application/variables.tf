variable "aws_region" {
  description = "AWS region for application infrastructure."
  type        = string
  default     = "sa-east-1"
}

variable "project_name" {
  type    = string
  default = "motordesk"
}

variable "environment" {
  type    = string
  default = "dev"

  validation {
    condition     = contains(["dev", "qa", "prod"], var.environment)
    error_message = "environment must be dev, qa, or prod."
  }
}

variable "vpc_cidr" {
  type    = string
  default = "10.40.0.0/16"
}

variable "public_subnet_cidrs" {
  type    = list(string)
  default = ["10.40.0.0/24", "10.40.1.0/24"]
}

variable "app_subnet_cidrs" {
  type    = list(string)
  default = ["10.40.10.0/24", "10.40.11.0/24"]
}

variable "data_subnet_cidrs" {
  type    = list(string)
  default = ["10.40.20.0/24", "10.40.21.0/24"]
}

variable "postgres_engine_version" {
  type    = string
  default = "16.4"
}

variable "postgres_instance_class" {
  type    = string
  default = "db.t4g.micro"
}

variable "postgres_admin_username" {
  type    = string
  default = "motordesk_admin"
}

variable "postgres_database_name" {
  type    = string
  default = "motordesk"
}

variable "documentdb_admin_username" {
  type    = string
  default = "motordesk_admin"
}

variable "documentdb_database_name" {
  type    = string
  default = "motordesk"
}

variable "documentdb_instance_class" {
  type    = string
  default = "db.t3.medium"
}

variable "documentdb_engine_version" {
  type    = string
  default = "5.0.0"
}

variable "documentdb_instance_count" {
  description = "DocumentDB instance count; use at least two for production availability."
  type        = number
  default     = 1
}

variable "redis_engine_version" {
  type    = string
  default = "7.1"
}

variable "redis_node_type" {
  type    = string
  default = "cache.t4g.micro"
}

variable "backup_retention_days" {
  type    = number
  default = 7
}

variable "apply_database_changes_immediately" {
  type    = bool
  default = false
}

variable "task_cpu" {
  type    = number
  default = 512
}

variable "task_memory" {
  type    = number
  default = 1024
}

variable "desired_count" {
  description = "Keep at zero until an image has been pushed to ECR."
  type        = number
  default     = 0
}

variable "image_tag" {
  type    = string
  default = "bootstrap"
}

variable "certificate_arn" {
  description = "ACM certificate ARN used by the HTTPS listener."
  type        = string

  validation {
    condition     = var.certificate_arn != ""
    error_message = "An ACM certificate ARN is required because the public load balancer only serves HTTPS."
  }
}

variable "ses_from_address" {
  description = "Email identity verified in Amazon SES for this AWS account and region."
  type        = string
}

variable "api_domain_name" {
  description = "DNS name covered by the ACM certificate and used to reach the API."
  type        = string
}

variable "route53_zone_id" {
  description = "Optional Route 53 hosted zone ID for an alias record."
  type        = string
  default     = ""
}

variable "documentdb_compatibility_confirmed" {
  description = "Operator confirmation that required MongoDB queries and indexes were validated against DocumentDB."
  type        = bool
  default     = false

  validation {
    condition     = var.environment == "dev" || var.documentdb_compatibility_confirmed
    error_message = "Validate DocumentDB compatibility before provisioning the QA or production service."
  }
}

variable "jwt_issuer" {
  type    = string
  default = "motordesk"
}

variable "jwt_audience" {
  type    = string
  default = "motordesk"
}

variable "jwt_realm" {
  type    = string
  default = "motordesk"
}

variable "github_oidc_subject" {
  description = "Exact GitHub Actions OIDC subject allowed to deploy, e.g. repo:owner/repo:ref:refs/heads/dev."
  type        = string
  default     = "repo:1ChristianAlex/motordesk:ref:refs/heads/dev"
}
