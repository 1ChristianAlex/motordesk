variable "aws_region" {
  description = "AWS region for the dev environment."
  type        = string
  default     = "us-east-2"
}

variable "ses_from_address" {
  description = "Sender identity verified in Amazon SES."
  type        = string
  default     = "christian.alexsander@outlook.com"
}

variable "api_domain_name" {
  description = "Optional custom API domain. When empty, dev uses the ALB-generated DNS name over HTTP."
  type        = string
  default     = ""
}

variable "route53_zone_id" {
  description = "Optional Route 53 hosted zone ID; leave empty to manage the DNS record externally."
  type        = string
  default     = ""
}

variable "mongo_database" {
  type    = string
  default = "motordesk"
}

variable "mongo_options" {
  type    = string
  default = ""
}
