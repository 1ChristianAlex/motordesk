package com.khrix.domain.serviceorder.model

enum class ServiceOrderStatus {
    CREATED,
    RECEIVED,
    WAITING_APPROVAL,
    REJECT_BY_CLIENT,
    IN_DIAGNOSIS,
    IN_PROGRESS,
    FINISHED,
    DELIVERED
}