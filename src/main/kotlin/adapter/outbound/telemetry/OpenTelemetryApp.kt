package com.khrix.adapter.outbound.telemetry

import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk
import io.opentelemetry.semconv.ServiceAttributes

internal fun getOpenTelemetry(serviceName: String): OpenTelemetry =
    AutoConfiguredOpenTelemetrySdk
        .builder()
        .addResourceCustomizer { oldResource, _ ->
            oldResource
                .toBuilder()
                .putAll(oldResource.attributes)
                .put(ServiceAttributes.SERVICE_NAME, serviceName)
                .build()
        }.build()
        .openTelemetrySdk
