variable "project_name" {
  description = "Project name used as a resource prefix."
  type        = string
  default     = "motordesk"
}

variable "environment" {
  description = "Deployment environment."
  type        = string
  default     = "dev"
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
