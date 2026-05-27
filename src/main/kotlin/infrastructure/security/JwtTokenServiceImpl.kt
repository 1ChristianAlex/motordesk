package com.khrix.infrastructure.security

import com.auth0.jwt.JWT
import com.khrix.domain.core.getCurrentUtcDateTime
import com.khrix.domain.security.TokenService
import com.khrix.domain.user.model.User
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.serialization.json.Json
import java.util.*
import kotlin.time.toJavaInstant

class JwtTokenServiceImpl(private val jwtConfig: JwtConfig) : TokenService {

    override fun generate(
        user: User
    ): String {
        val expiration = getCurrentUtcDateTime().date.plus(1, DateTimeUnit.DAY)
        val javaInstant = expiration.atStartOfDayIn(TimeZone.UTC).toJavaInstant()
        val claims = UserClaims.toClaims(user)

        val token = JWT.create()
            .withAudience(jwtConfig.audience)
            .withIssuer(jwtConfig.issuer)
            .withPayload(Json.encodeToString(claims))
            .withExpiresAt(Date.from(javaInstant))
            .sign(jwtConfig.algorithm)

        return token
    }
}
