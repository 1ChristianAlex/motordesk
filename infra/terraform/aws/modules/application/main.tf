terraform {
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

data "aws_partition" "current" {}
data "aws_availability_zones" "available" {
  state = "available"
}

locals {
  availability_zones = slice(data.aws_availability_zones.available.names, 0, 2)
}

resource "aws_ecr_repository" "api" {
  name                 = "${var.project_name}/${var.environment}/api"
  image_tag_mutability = "IMMUTABLE"

  image_scanning_configuration {
    scan_on_push = true
  }

  encryption_configuration {
    encryption_type = "AES256"
  }
}

resource "aws_ecr_lifecycle_policy" "api" {
  repository = aws_ecr_repository.api.name
  policy = jsonencode({
    rules = [{
      rulePriority = 1
      description  = "Retain the most recent 2 images"
      selection = {
        tagStatus   = "any"
        countType   = "imageCountMoreThan"
        countNumber = 2
      }


      action = {
        type = "expire"
      }
    }]
  })
}

resource "aws_cloudwatch_log_group" "api" {
  name              = "/ecs/${var.project_name}/${var.environment}/api"
  retention_in_days = 30
}

resource "aws_secretsmanager_secret" "documentdb_password" {
  name                    = "${var.project_name}/${var.environment}/documentdb-password"
  recovery_window_in_days = 7
}

resource "random_password" "documentdb" {
  length  = 32
  special = false
}

resource "aws_secretsmanager_secret_version" "documentdb_password" {
  secret_id     = aws_secretsmanager_secret.documentdb_password.id
  secret_string = random_password.documentdb.result
}

resource "aws_secretsmanager_secret" "redis_auth_token" {
  name                    = "${var.project_name}/${var.environment}/redis-auth-token"
  recovery_window_in_days = 7
}

resource "random_password" "redis_auth_token" {
  length  = 32
  special = false
}

resource "aws_secretsmanager_secret_version" "redis_auth_token" {
  secret_id     = aws_secretsmanager_secret.redis_auth_token.id
  secret_string = random_password.redis_auth_token.result
}

resource "aws_secretsmanager_secret" "jwt_secret" {
  name                    = "${var.project_name}/${var.environment}/jwt-secret"
  recovery_window_in_days = 7
}

resource "random_password" "jwt_secret" {
  length  = 64
  special = false
}

resource "aws_secretsmanager_secret_version" "jwt_secret" {
  secret_id     = aws_secretsmanager_secret.jwt_secret.id
  secret_string = random_password.jwt_secret.result
}

resource "aws_ssm_parameter" "aws_region" {
  name  = "/${var.project_name}/${var.environment}/aws-region"
  type  = "String"
  value = var.aws_region
}

resource "aws_ssm_parameter" "ses_from_address" {
  name  = "/${var.project_name}/${var.environment}/ses-from-address"
  type  = "String"
  value = var.ses_from_address
}

resource "aws_ssm_parameter" "jwt_issuer" {
  name  = "/${var.project_name}/${var.environment}/jwt-issuer"
  type  = "String"
  value = var.jwt_issuer
}

resource "aws_ssm_parameter" "jwt_audience" {
  name  = "/${var.project_name}/${var.environment}/jwt-audience"
  type  = "String"
  value = var.jwt_audience
}

resource "aws_ssm_parameter" "jwt_realm" {
  name  = "/${var.project_name}/${var.environment}/jwt-realm"
  type  = "String"
  value = var.jwt_realm
}

resource "aws_db_subnet_group" "postgres" {
  name       = "${var.project_name}-${var.environment}-postgres"
  subnet_ids = aws_subnet.data[*].id
}

resource "aws_security_group" "postgres" {
  name        = "${var.project_name}-${var.environment}-postgres"
  description = "PostgreSQL access only from the application service."
  vpc_id      = aws_vpc.main.id

  ingress {
    description     = "PostgreSQL from ECS tasks"
    from_port       = 5432
    to_port         = 5432
    protocol        = "tcp"
    security_groups = [aws_security_group.tasks.id]
  }

  egress = []
}

resource "aws_db_instance" "postgres" {
  identifier                  = "${var.project_name}-${var.environment}-postgres"
  engine                      = "postgres"
  engine_version              = var.postgres_engine_version
  instance_class              = var.postgres_instance_class
  allocated_storage           = 20
  max_allocated_storage       = 100
  storage_encrypted           = true
  db_name                     = var.postgres_database_name
  username                    = var.postgres_admin_username
  manage_master_user_password = true
  db_subnet_group_name        = aws_db_subnet_group.postgres.name
  vpc_security_group_ids      = [aws_security_group.postgres.id]
  publicly_accessible         = false
  multi_az                    = var.environment == "prod"
  backup_retention_period     = var.backup_retention_days
  deletion_protection         = var.environment == "prod"
  skip_final_snapshot         = var.environment != "prod"
  final_snapshot_identifier   = var.environment == "prod" ? "${var.project_name}-${var.environment}-final" : null
  apply_immediately           = var.apply_database_changes_immediately

  lifecycle {
    prevent_destroy = true
  }
}

resource "aws_docdb_subnet_group" "main" {
  name       = "${var.project_name}-${var.environment}-documentdb"
  subnet_ids = aws_subnet.data[*].id
}

resource "aws_security_group" "documentdb" {
  name        = "${var.project_name}-${var.environment}-documentdb"
  description = "DocumentDB access only from the application service."
  vpc_id      = aws_vpc.main.id

  ingress {
    description     = "MongoDB-compatible access from ECS tasks"
    from_port       = 27017
    to_port         = 27017
    protocol        = "tcp"
    security_groups = [aws_security_group.tasks.id]
  }

  egress = []
}

resource "aws_docdb_cluster" "main" {
  cluster_identifier              = "${var.project_name}-${var.environment}-documentdb"
  engine                          = "docdb"
  engine_version                  = var.documentdb_engine_version
  master_username                 = var.documentdb_admin_username
  master_password                 = random_password.documentdb.result
  db_subnet_group_name            = aws_docdb_subnet_group.main.name
  vpc_security_group_ids          = [aws_security_group.documentdb.id]
  storage_encrypted               = true
  backup_retention_period         = var.backup_retention_days
  preferred_backup_window         = "03:00-04:00"
  preferred_maintenance_window    = "sun:04:00-sun:05:00"
  deletion_protection             = var.environment == "prod"
  skip_final_snapshot             = var.environment != "prod"
  final_snapshot_identifier       = var.environment == "prod" ? "${var.project_name}-${var.environment}-documentdb-final" : null
  apply_immediately               = var.apply_database_changes_immediately
  db_cluster_parameter_group_name = aws_docdb_cluster_parameter_group.main.name

  lifecycle {
    prevent_destroy = true
  }
}

resource "aws_docdb_cluster_parameter_group" "main" {
  family = "docdb5.0"
  name   = "${var.project_name}-${var.environment}-docdb"

  parameter {
    name  = "tls"
    value = "enabled"
  }
}

resource "aws_docdb_cluster_instance" "main" {
  count              = var.environment == "prod" ? 2 : var.documentdb_instance_count
  identifier         = "${var.project_name}-${var.environment}-documentdb-${count.index + 1}"
  cluster_identifier = aws_docdb_cluster.main.id
  instance_class     = var.documentdb_instance_class
}

resource "aws_elasticache_subnet_group" "main" {
  name       = "${var.project_name}-${var.environment}-redis"
  subnet_ids = aws_subnet.data[*].id
}

resource "aws_security_group" "redis" {
  name        = "${var.project_name}-${var.environment}-redis"
  description = "Redis access only from the application service."
  vpc_id      = aws_vpc.main.id

  ingress {
    description     = "Redis TLS from ECS tasks"
    from_port       = 6379
    to_port         = 6379
    protocol        = "tcp"
    security_groups = [aws_security_group.tasks.id]
  }

  egress = []
}

resource "aws_elasticache_replication_group" "redis" {
  replication_group_id       = "${var.project_name}-${var.environment}"
  description                = "Motor Desk Redis Streams"
  engine                     = "redis"
  engine_version             = var.redis_engine_version
  node_type                  = var.environment == "prod" ? "cache.t4g.small" : var.redis_node_type
  port                       = 6379
  num_cache_clusters         = var.environment == "prod" ? 2 : 1
  parameter_group_name       = "default.redis7"
  subnet_group_name          = aws_elasticache_subnet_group.main.name
  security_group_ids         = [aws_security_group.redis.id]
  at_rest_encryption_enabled = true
  transit_encryption_enabled = true
  auth_token                 = random_password.redis_auth_token.result
  automatic_failover_enabled = var.environment == "prod"
  multi_az_enabled           = var.environment == "prod"
  apply_immediately          = var.apply_database_changes_immediately
  snapshot_retention_limit   = var.backup_retention_days
  snapshot_window            = "02:00-03:00"
  maintenance_window         = "sun:05:00-sun:06:00"

  lifecycle {
    prevent_destroy = true
  }
}
