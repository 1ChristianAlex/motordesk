package com.khrix.domain.email.publisher

enum class EventKeys(
    val value: String,
) {
    EVENT_TYPE("email"),
    EVENT_GROUP("email-workers"),

    APPROVAL_EVENT_NAME("APPROVAL_EVENT_NAME"),
    UPDATE_EVENT_NAME("UPDATE_EVENT_NAME"),
}
