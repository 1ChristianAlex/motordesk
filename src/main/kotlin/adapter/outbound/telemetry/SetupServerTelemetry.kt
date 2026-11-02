package com.khrix.adapter.outbound.telemetry

import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import io.ktor.util.date.getTimeMillis
import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.api.trace.SpanKind
import io.opentelemetry.api.trace.StatusCode
import io.opentelemetry.instrumentation.ktor.v3_0.KtorServerTelemetry

fun Application.setupServerTelemetry(): OpenTelemetry {
    val serviceName = System.getenv("OTEL_SERVICE_NAME") ?: "opentelemetry-motordesk"
    val openTelemetry = getOpenTelemetry(serviceName)

    install(KtorServerTelemetry) {
        setOpenTelemetry(openTelemetry)

        knownMethods(HttpMethod.DefaultMethods)

        capturedRequestHeaders(HttpHeaders.UserAgent)
        capturedResponseHeaders(HttpHeaders.ContentType)

        spanStatusExtractor {
            val path = response?.call?.request?.path() ?: ""
            if (path.contains("/span-status-extractor") || error != null) {
                spanStatusBuilder.setStatus(StatusCode.ERROR)
            }
        }

        spanKindExtractor {
            if (httpMethod == HttpMethod.Post) {
                SpanKind.PRODUCER
            } else {
                SpanKind.CLIENT
            }
        }

        attributesExtractor {
            onStart {
                attributes.put("start-time", getTimeMillis())
            }
            onEnd {
                attributes.put("end-time", getTimeMillis())
            }
        }
    }
    return openTelemetry
}
