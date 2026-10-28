variable "aws_region" {
  description = "AWS region for the production environment."
  type        = string
  default     = "us-east-2"
}

variable "ses_from_address" {
  description = "Sender identity verified in Amazon SES."
  type        = string
  default     = "christian.alexsander@outlook.com"
}

variable "api_domain_name" {
  description = "DNS name used for the API and its ACM certificate."
  type        = string
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
