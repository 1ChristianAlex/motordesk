resource "aws_iam_role" "ecs_execution" {
  name = "${var.project_name}-${var.environment}-ecs-execution"
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Effect = "Allow"
      Principal = {
        Service = "ecs-tasks.amazonaws.com"
      }
      Action = "sts:AssumeRole"
    }]
  })
}

resource "aws_iam_role_policy_attachment" "ecs_execution" {
  role       = aws_iam_role.ecs_execution.name
  policy_arn = "arn:${data.aws_partition.current.partition}:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

resource "aws_iam_role_policy" "ecs_secrets" {
  name = "read-runtime-secrets"
  role = aws_iam_role.ecs_execution.id
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = ["secretsmanager:GetSecretValue"]
        Resource = [
          aws_db_instance.postgres.master_user_secret[0].secret_arn,
          aws_secretsmanager_secret.redis_auth_token.arn,
          aws_secretsmanager_secret.jwt_secret.arn,
          aws_secretsmanager_secret.mongo_credentials.arn
        ]
      },
      {
        Effect = "Allow"
        Action = ["ssm:GetParameters"]
        Resource = [
          aws_ssm_parameter.aws_region.arn,
          aws_ssm_parameter.ses_from_address.arn,
          aws_ssm_parameter.jwt_issuer.arn,
          aws_ssm_parameter.jwt_audience.arn,
          aws_ssm_parameter.jwt_realm.arn
        ]
      }
    ]
  })
}

resource "aws_iam_role" "ecs_task" {
  name = "${var.project_name}-${var.environment}-ecs-task"
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Effect = "Allow"
      Principal = {
        Service = "ecs-tasks.amazonaws.com"
      }
      Action = "sts:AssumeRole"
    }]
  })
}

resource "aws_iam_role_policy" "ses_send" {
  name = "send-transactional-email"
  role = aws_iam_role.ecs_task.id
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Effect   = "Allow"
      Action   = ["ses:SendEmail"]
      Resource = aws_sesv2_email_identity.sender.arn
    }]
  })
}

resource "aws_iam_role" "mongo_task" {
  name = "${var.project_name}-${var.environment}-mongo-task"
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Effect = "Allow"
      Principal = {
        Service = "ecs-tasks.amazonaws.com"
      }
      Action = "sts:AssumeRole"
    }]
  })
}

resource "aws_iam_role_policy" "mongo_efs" {
  name = "mount-mongo-efs"
  role = aws_iam_role.mongo_task.id
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Effect = "Allow"
      Action = [
        "elasticfilesystem:ClientMount",
        "elasticfilesystem:ClientWrite"
      ]
      Resource = aws_efs_file_system.mongo.arn
      Condition = {
        StringEquals = {
          "elasticfilesystem:AccessPointArn" = aws_efs_access_point.mongo.arn
        }
      }
    }]
  })
}

resource "aws_ecs_cluster" "main" {
  name = "${var.project_name}-${var.environment}"

  setting {
    name  = "containerInsights"
    value = "enabled"
  }
}

resource "aws_ecs_task_definition" "api" {
  family                   = "${var.project_name}-${var.environment}-api"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = var.task_cpu
  memory                   = var.task_memory
  execution_role_arn       = aws_iam_role.ecs_execution.arn
  task_role_arn            = aws_iam_role.ecs_task.arn

  container_definitions = jsonencode([{
    name      = "api"
    image     = "${aws_ecr_repository.api.repository_url}:${var.image_tag}"
    essential = true
    portMappings = [{
      containerPort = 8080
      hostPort      = 8080
      protocol      = "tcp"
    }]
    environment = [
      { name = "HTTP_HOST", value = "0.0.0.0" },
      { name = "HTTP_PORT", value = "8080" },
      { name = "HTTP_PROTOCOL", value = "http" },
      { name = "DATABASE_DRIVER", value = "org.postgresql.Driver" },
      { name = "DATABASE_URL", value = "jdbc:postgresql://${aws_db_instance.postgres.address}:${aws_db_instance.postgres.port}/${var.postgres_database_name}" },
      { name = "DATABASE_USER", value = var.postgres_admin_username },
      { name = "MONGO_URL", value = "${aws_service_discovery_service.mongo.name}.${aws_service_discovery_private_dns_namespace.api.name}:27017" },
      { name = "MONGO_DATABASE", value = var.mongo_database },
      { name = "MONGO_OPTIONS", value = var.mongo_options },
      { name = "REDIS_HOST", value = aws_elasticache_replication_group.redis.primary_endpoint_address },
      { name = "REDIS_PORT", value = "6379" },
      { name = "REDIS_TLS", value = "true" },
    ]
    secrets = [
      { name = "DATABASE_PASSWORD", valueFrom = "${aws_db_instance.postgres.master_user_secret[0].secret_arn}:password::" },
      { name = "REDIS_PASSWORD", valueFrom = aws_secretsmanager_secret.redis_auth_token.arn },
      { name = "JWT_SECRET", valueFrom = aws_secretsmanager_secret.jwt_secret.arn },
      { name = "MONGO_USER", valueFrom = "${aws_secretsmanager_secret.mongo_credentials.arn}:username::" },
      { name = "MONGO_PASSWORD", valueFrom = "${aws_secretsmanager_secret.mongo_credentials.arn}:password::" },
      { name = "AWS_REGION", valueFrom = aws_ssm_parameter.aws_region.arn },
      { name = "AWS_SES_FROM_ADDRESS", valueFrom = aws_ssm_parameter.ses_from_address.arn },
      { name = "JWT_ISSUER", valueFrom = aws_ssm_parameter.jwt_issuer.arn },
      { name = "JWT_AUDIENCE", valueFrom = aws_ssm_parameter.jwt_audience.arn },
      { name = "JWT_REALM", valueFrom = aws_ssm_parameter.jwt_realm.arn }
    ]
    logConfiguration = {
      logDriver = "awslogs"
      options = {
        "awslogs-group"         = aws_cloudwatch_log_group.api.name
        "awslogs-region"        = var.aws_region
        "awslogs-stream-prefix" = "api"
      }
    }
  }])

  depends_on = [
    aws_iam_role_policy_attachment.ecs_execution,
    aws_iam_role_policy.ecs_secrets,
    aws_secretsmanager_secret_version.mongo_credentials
  ]
}

resource "aws_ecs_service" "api" {
  name            = "${var.project_name}-${var.environment}-api"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.api.arn
  desired_count   = var.desired_count
  launch_type     = "FARGATE"

  service_registries {
    registry_arn   = aws_service_discovery_service.api.arn
    container_name = "api"
    container_port = 8080
  }

  network_configuration {
    subnets          = aws_subnet.app[*].id
    security_groups  = [aws_security_group.tasks.id]
    assign_public_ip = false
  }

  depends_on = [aws_ecs_service.mongo]

  lifecycle {
    ignore_changes = [task_definition, desired_count]
  }
}

resource "aws_cloudwatch_log_group" "mongo" {
  name              = "/ecs/${var.project_name}/${var.environment}/mongo"
  retention_in_days = 7
}

resource "aws_ecs_task_definition" "mongo" {
  family                   = "${var.project_name}-${var.environment}-mongo"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = 512
  memory                   = 1024
  execution_role_arn       = aws_iam_role.ecs_execution.arn
  task_role_arn            = aws_iam_role.mongo_task.arn

  volume {
    name = "mongo-data"

    efs_volume_configuration {
      file_system_id     = aws_efs_file_system.mongo.id
      root_directory     = "/"
      transit_encryption = "ENABLED"

      authorization_config {
        access_point_id = aws_efs_access_point.mongo.id
        iam             = "ENABLED"
      }
    }
  }

  container_definitions = jsonencode([{
    name      = "mongo"
    image     = "mongo:8.0.34"
    essential = true
    portMappings = [{
      containerPort = 27017
      hostPort      = 27017
      protocol      = "tcp"
    }]
    mountPoints = [{
      sourceVolume  = "mongo-data"
      containerPath = "/data/db"
      readOnly      = false
    }]
    environment = [
      { name = "MONGO_INITDB_DATABASE", value = var.mongo_database }
    ]
    secrets = [
      { name = "MONGO_INITDB_ROOT_USERNAME", valueFrom = "${aws_secretsmanager_secret.mongo_credentials.arn}:root_username::" },
      { name = "MONGO_INITDB_ROOT_PASSWORD", valueFrom = "${aws_secretsmanager_secret.mongo_credentials.arn}:root_password::" }
    ]
    healthCheck = {
      command     = ["CMD-SHELL", "mongosh --quiet --eval 'db.adminCommand(\"ping\").ok'"]
      interval    = 30
      timeout     = 5
      retries     = 3
      startPeriod = 60
    }
    logConfiguration = {
      logDriver = "awslogs"
      options = {
        "awslogs-group"         = aws_cloudwatch_log_group.mongo.name
        "awslogs-region"        = var.aws_region
        "awslogs-stream-prefix" = "mongo"
      }
    }
    }, {
    name       = "mongo-user-init"
    image      = "mongo:8.0.34"
    essential  = true
    entryPoint = ["/bin/bash", "-lc"]
    command = [
      "set -e; mongosh --quiet --host 127.0.0.1 --port 27017 --username \"$MONGO_ROOT_USERNAME\" --password \"$MONGO_ROOT_PASSWORD\" --authenticationDatabase admin --eval 'const authDb = db.getSiblingDB(\"admin\"); const user = process.env.MONGO_APP_USERNAME; const options = { pwd: process.env.MONGO_APP_PASSWORD, roles: [{ role: \"readWrite\", db: process.env.MONGO_DATABASE }] }; if (authDb.getUser(user)) authDb.updateUser(user, options); else authDb.createUser({ user, ...options });'; exec sleep infinity"
    ]
    environment = [
      { name = "MONGO_DATABASE", value = var.mongo_database }
    ]
    secrets = [
      { name = "MONGO_ROOT_USERNAME", valueFrom = "${aws_secretsmanager_secret.mongo_credentials.arn}:root_username::" },
      { name = "MONGO_ROOT_PASSWORD", valueFrom = "${aws_secretsmanager_secret.mongo_credentials.arn}:root_password::" },
      { name = "MONGO_APP_USERNAME", valueFrom = "${aws_secretsmanager_secret.mongo_credentials.arn}:username::" },
      { name = "MONGO_APP_PASSWORD", valueFrom = "${aws_secretsmanager_secret.mongo_credentials.arn}:password::" }
    ]
    dependsOn = [{
      containerName = "mongo"
      condition     = "HEALTHY"
    }]
    logConfiguration = {
      logDriver = "awslogs"
      options = {
        "awslogs-group"         = aws_cloudwatch_log_group.mongo.name
        "awslogs-region"        = var.aws_region
        "awslogs-stream-prefix" = "mongo-user-init"
      }
    }
  }])

  depends_on = [
    aws_iam_role_policy_attachment.ecs_execution,
    aws_iam_role_policy.ecs_secrets,
    aws_iam_role_policy.mongo_efs,
    aws_secretsmanager_secret_version.mongo_credentials
  ]
}

resource "aws_ecs_service" "mongo" {
  name                               = "${var.project_name}-${var.environment}-mongo"
  cluster                            = aws_ecs_cluster.main.id
  task_definition                    = aws_ecs_task_definition.mongo.arn
  desired_count                      = 1
  launch_type                        = "FARGATE"
  deployment_minimum_healthy_percent = 0
  deployment_maximum_percent         = 100

  service_registries {
    registry_arn = aws_service_discovery_service.mongo.arn
  }

  network_configuration {
    subnets          = aws_subnet.app[*].id
    security_groups  = [aws_security_group.mongo.id]
    assign_public_ip = false
  }

  depends_on = [aws_efs_mount_target.mongo]
}

resource "aws_iam_user" "github_deploy" {
  name = "${var.project_name}-${var.environment}-github-deploy"
}

resource "aws_iam_user_policy" "github_deploy" {
  name = "publish-and-deploy-api"
  user = aws_iam_user.github_deploy.name
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect   = "Allow"
        Action   = ["ecr:GetAuthorizationToken"]
        Resource = "*"
      },
      {
        Effect = "Allow"
        Action = [
          "ecr:BatchCheckLayerAvailability",
          "ecr:CompleteLayerUpload",
          "ecr:InitiateLayerUpload",
          "ecr:PutImage",
          "ecr:UploadLayerPart"
        ]
        Resource = aws_ecr_repository.api.arn
      },
      {
        Effect = "Allow"
        Action = [
          "ecs:RegisterTaskDefinition",
          "ecs:DescribeTaskDefinition"
        ]
        Resource = "*"
      },
      {
        Effect = "Allow"
        Action = ["ecs:DescribeServices"]
        Resource = [
          aws_ecs_cluster.main.arn,
          aws_ecs_service.api.id
        ]
      },
      {
        Effect   = "Allow"
        Action   = ["ecs:UpdateService"]
        Resource = [aws_ecs_service.api.id]
      },
      {
        Effect   = "Allow"
        Action   = ["iam:PassRole"]
        Resource = [aws_iam_role.ecs_execution.arn, aws_iam_role.ecs_task.arn]
        Condition = {
          StringEquals = {
            "iam:PassedToService" = "ecs-tasks.amazonaws.com"
          }
        }
      }
    ]
  })
}
