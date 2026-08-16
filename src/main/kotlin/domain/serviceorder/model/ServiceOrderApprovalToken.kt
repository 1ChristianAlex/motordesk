package com.khrix.domain.serviceorder.model

import com.khrix.domain.user.security.SecurityHasher
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours
import kotlin.time.Instant

data class ServiceOrderApprovalToken(
    val id: Int,
    val serviceOrderCode: String,
    val tokenHash: String,
    val expiresAt: Instant,
    val usedAt: Instant?,
    val revokedAt: Instant?,
) {
    companion object {
        private const val SALT_HASH = "SALT_THIS_THING"

        fun generateTokenHash(
            securityHasher: SecurityHasher,
            serviceOrderCode: String,
        ): String = securityHasher.hash(serviceOrderCode + SALT_HASH)

        fun generateExpiresAt(): Instant = Clock.System.now().plus(3.hours)
    }

    constructor(
        serviceOrderCode: String,
        securityHasher: SecurityHasher,
    ) : this(
        0,
        serviceOrderCode,
        generateTokenHash(securityHasher, serviceOrderCode),
        generateExpiresAt(),
        null,
        null,
    )
}
