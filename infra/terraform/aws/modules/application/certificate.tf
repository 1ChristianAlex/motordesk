resource "aws_acm_certificate" "api" {
  count             = var.api_domain_name == "" ? 0 : 1
  domain_name       = var.api_domain_name
  validation_method = "DNS"

  lifecycle {
    create_before_destroy = true
  }
}

resource "aws_route53_record" "certificate_validation" {
  for_each = var.route53_zone_id == "" || var.api_domain_name == "" ? {} : {
    for option in aws_acm_certificate.api[0].domain_validation_options : option.domain_name => option
  }

  zone_id         = var.route53_zone_id
  name            = each.value.resource_record_name
  type            = each.value.resource_record_type
  records         = [each.value.resource_record_value]
  ttl             = 60
  allow_overwrite = true
}

resource "aws_acm_certificate_validation" "api" {
  count           = var.api_domain_name == "" ? 0 : 1
  certificate_arn = aws_acm_certificate.api[0].arn
  validation_record_fqdns = var.route53_zone_id == "" ? [] : [
    for record in aws_route53_record.certificate_validation : record.fqdn
  ]
}

resource "aws_apigatewayv2_domain_name" "api" {
  count       = var.api_domain_name == "" ? 0 : 1
  domain_name = var.api_domain_name

  domain_name_configuration {
    certificate_arn = aws_acm_certificate_validation.api[0].certificate_arn
    endpoint_type   = "REGIONAL"
    security_policy = "TLS_1_2"
  }
}

resource "aws_apigatewayv2_api_mapping" "api" {
  count       = var.api_domain_name == "" ? 0 : 1
  api_id      = aws_apigatewayv2_api.api.id
  domain_name = aws_apigatewayv2_domain_name.api[0].id
  stage       = aws_apigatewayv2_stage.api.name
}

resource "aws_route53_record" "api" {
  count   = var.route53_zone_id == "" || var.api_domain_name == "" ? 0 : 1
  zone_id = var.route53_zone_id
  name    = var.api_domain_name
  type    = "A"

  alias {
    name                   = aws_apigatewayv2_domain_name.api[0].domain_name_configuration[0].target_domain_name
    zone_id                = aws_apigatewayv2_domain_name.api[0].domain_name_configuration[0].hosted_zone_id
    evaluate_target_health = false
  }
}
