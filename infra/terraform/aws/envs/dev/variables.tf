variable "aws_region" {
  description = "AWS region for the dev environment."
  type        = string
  default     = "sa-east-1"
}

variable "certificate_arn" {
  description = "ACM certificate ARN for HTTPS."
  type        = string
}

variable "ses_from_address" {
  description = "Sender identity verified in Amazon SES."
  type        = string
}

variable "api_domain_name" {
  description = "DNS name covered by certificate_arn and used to reach the API."
  type        = string
}

variable "route53_zone_id" {
  description = "Optional Route 53 hosted zone ID; leave empty to manage the DNS record externally."
  type        = string
  default     = ""
}

variable "github_oidc_subject" {
  description = "Exact GitHub Actions OIDC subject allowed to deploy."
  type        = string
  default     = "repo:1ChristianAlex/motordesk:ref:refs/heads/dev"
}
