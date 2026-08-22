package com.khrix.adapter.outbound.exposed

enum class DatabaseSchemas(
    val value: String,
) {
    IDENTITY("identity"),
    VEHICLES("vehicles"),
    INVENTORY("inventory"),
    SERVICE_ORDER("service_order"),
    EMAIL("email"),
}
