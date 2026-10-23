terraform {
  required_version = ">= 1.10.0"

  backend "s3" {
    key          = "motordesk/qa/terraform.tfstate"
    encrypt      = true
    use_lockfile = true
  }

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "6.0.0"
    }
    random = {
      source  = "hashicorp/random"
      version = "3.7.2"
    }
  }
}

provider "aws" {
  region = var.aws_region

  default_tags {
    tags = {
      Project     = "motordesk"
      Environment = "qa"
      ManagedBy   = "terraform"
    }
  }
}

module "application" {
  source = "../../modules/application"

  environment                        = "qa"
  aws_region                         = var.aws_region
  certificate_arn                    = var.certificate_arn
  ses_from_address                   = var.ses_from_address
  github_oidc_subject                = var.github_oidc_subject
  api_domain_name                    = var.api_domain_name
  route53_zone_id                    = var.route53_zone_id
  documentdb_compatibility_confirmed = var.documentdb_compatibility_confirmed
}
