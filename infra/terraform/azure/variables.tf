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

variable "environments" {
  description = "Possible environment"
  type        = map(string)

  default = {
    dev  = "dev"
    qa   = "qa"
    prod = "prod"
  }
}

variable "environment" {
  description = "Deployment environment."
  type        = string
  default     = "dev"

  validation {
    condition     = contains(values(var.environments), var.environment)
    error_message = "environment must match one of the values defined in environments."
  }
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

variable "mongo_admin_username" {
  description = "Mongo administrator username."
  type        = string
  sensitive   = true
  default     = "mongoadmin"
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

variable "dev_client_ip" {
  description = "Public IP address allowed to connect to the dev PostgreSQL server."
  type        = string
  default     = "0.0.0.0"
}

# variable "aks_node_count" {
#   description = "Initial number of AKS nodes."
#   type        = number
#   default     = 1
# }
#
# variable "aks_vm_size" {
#   description = "VM size used by the AKS system node pool."
#   type        = string
#   default     = "Standard_F2"
# }

