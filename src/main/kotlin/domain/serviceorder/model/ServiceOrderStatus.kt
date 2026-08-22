package com.khrix.domain.serviceorder.model

import com.khrix.domain.user.model.Role

enum class ServiceOrderStatus {
    CREATED,
    WAITING_APPROVAL,
    REJECT_BY_CLIENT,
    IN_DIAGNOSIS,
    IN_PROGRESS,
    FINISHED,
    DELIVERED,
    CANCELLED,
    ;

    fun needApproval(): Boolean {
        val requireApprovals =
            listOf(
                CREATED,
                WAITING_APPROVAL,
            )

        return this in requireApprovals
    }

    private fun getAllowStatusByRole(role: Role): List<ServiceOrderStatus> =
        when (role) {
            Role.ADMIN,
            Role.MANAGER,
            -> {
                entries
            }

            Role.ENGINEER -> {
                listOf(
                    IN_DIAGNOSIS,
                    IN_PROGRESS,
                    FINISHED,
                    DELIVERED,
                )
            }

            else -> {
                listOf()
            }
        }

    fun checkRole(role: Role): Boolean = this in getAllowStatusByRole(role)
}
