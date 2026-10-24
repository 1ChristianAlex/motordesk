terraform {
  required_version = ">= 1.10.0"

  backend "s3" {
    key          = "motordesk/prod/terraform.tfstate"
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
      Environment = "prod"
      ManagedBy   = "terraform"
    }
  }
}

module "application" {
  source = "../../modules/application"

  environment      = "prod"
  aws_region       = var.aws_region
  ses_from_address = var.ses_from_address
  api_domain_name  = var.api_domain_name
  route53_zone_id  = var.route53_zone_id
}
