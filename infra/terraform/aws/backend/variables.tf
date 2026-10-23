variable "aws_region" {
  description = "AWS region used for the Terraform state bucket."
  type        = string
  default     = "sa-east-1"
}

variable "state_bucket_name" {
  description = "Globally unique S3 bucket name for encrypted, versioned Terraform state."
  type        = string
}
