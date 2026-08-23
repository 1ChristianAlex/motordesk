variable "infra_location" {
  description = "Azure motordesk region."
  type        = string
  default     = "brazilsouth"
}

variable "project_name" {
  description = "Project name used as a resource prefix."
  type        = string
  default     = "motordesk"
}

variable "environment" {
  description = "Deployment environment."
  type        = string
  default     = "dev-test"
}

variable "postgres_version" {
  description = "PostgreSQL major version."
  type        = string
  default     = "16"
}

variable "postgres_admin_username" {
  description = "PostgreSQL administrator username."
  type        = string
  sensitive   = true
  default     = "psqladmin"
}

variable "postgres_admin_password" {
  description = "PostgreSQL administrator password."
  type        = string
  sensitive   = true
  default     = "5@K{9/)t=#)9"
}

variable "postgres_database_name" {
  description = "Application PostgreSQL database."
  type        = string
  default     = "motordesk"
}

variable "tags" {
  description = "Common Azure resource tags."
  type        = map(string)

  default = {
    project     = "motordesk"
    managed_by  = "terraform"
    environment = "dev"
  }
}
variable "resource_name_email" {
  type    = string
  default = "motordesk-email-service"
}

variable "no_replay_username" {
  type    = string
  default = "noreplay-sys-email"
}
