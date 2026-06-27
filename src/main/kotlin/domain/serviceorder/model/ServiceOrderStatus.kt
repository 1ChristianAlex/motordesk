package com.khrix.domain.serviceorder.model

import com.khrix.domain.user.model.Role

enum class ServiceOrderStatus {
    CREATED,
    RECEIVED,
    WAITING_APPROVAL,
    REJECT_BY_CLIENT,
    IN_DIAGNOSIS,
    IN_PROGRESS,
    FINISHED,
    DELIVERED,
    CANCELLED;

    private fun getAllowStatusByRole(role: Role): List<ServiceOrderStatus> {
        return when (role) {
            Role.ADMIN,
            Role.MANAGER -> entries

            Role.ENGINEER -> listOf(
                IN_DIAGNOSIS,
                IN_PROGRESS,
                FINISHED,
                DELIVERED,
            )

            else -> listOf()
        }
    }

    fun checkRole(role: Role): Boolean {
        return this in getAllowStatusByRole(role)
    }
}

