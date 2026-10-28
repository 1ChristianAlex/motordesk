variable "aws_region" {
  description = "AWS region for application infrastructure."
  type        = string
  default     = "us-east-2"
}

variable "project_name" {
  type    = string
  default = "motordesk"
}

variable "environment" {
  type    = string
  default = "dev"

  validation {
    condition     = contains(["dev-cli", "qa", "prod"], var.environment)
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

variable "mongo_database" {
  type    = string
  default = "motordesk"
}

variable "mongo_options" {
  description = "Optional MongoDB connection options, for example tls=true."
  type        = string
  default     = ""
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
  default = ""
}

variable "ses_from_address" {
  description = "Email identity verified in Amazon SES for this AWS account and region."
  type        = string
  default     = "christian.alexsander@outlook.com"
}

variable "api_domain_name" {
  description = "DNS name used for the API and its ACM certificate."
  type        = string
  default     = ""

  validation {
    condition     = var.api_domain_name != "" || var.environment == "dev"
    error_message = "api_domain_name is required outside the dev environment."
  }
}

variable "route53_zone_id" {
  description = "Optional Route 53 hosted zone ID for an alias record."
  type        = string
  default     = ""
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
