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
          aws_secretsmanager_secret.jwt_secret.arn
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

resource "aws_iam_role_policy" "dynamodb_history" {
  name = "access-history-table"
  role = aws_iam_role.ecs_task.id
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Effect = "Allow"
      Action = [
        "dynamodb:PutItem",
        "dynamodb:Query"
      ]
      Resource = aws_dynamodb_table.history.arn
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
      { name = "DYNAMODB_HISTORY_TABLE", value = aws_dynamodb_table.history.name },
      { name = "REDIS_HOST", value = aws_elasticache_replication_group.redis.primary_endpoint_address },
      { name = "REDIS_PORT", value = "6379" },
      { name = "REDIS_TLS", value = "true" },
    ]
    secrets = [
      { name = "DATABASE_PASSWORD", valueFrom = "${aws_db_instance.postgres.master_user_secret[0].secret_arn}:password::" },
      { name = "REDIS_PASSWORD", valueFrom = aws_secretsmanager_secret.redis_auth_token.arn },
      { name = "JWT_SECRET", valueFrom = aws_secretsmanager_secret.jwt_secret.arn },
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
    aws_iam_role_policy.dynamodb_history
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

  lifecycle {
    ignore_changes = [task_definition, desired_count]
  }
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
