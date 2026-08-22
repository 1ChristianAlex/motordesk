package com.khrix.adapter.outbound.security

import com.auth0.jwt.JWT
import com.khrix.adapter.app.InfraConfig
import com.khrix.domain.core.getCurrentUtcDateTime
import com.khrix.domain.user.model.User
import com.khrix.domain.user.security.TokenService
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.serialization.json.Json
import java.util.Date
import kotlin.time.toJavaInstant

class JwtTokenServiceImpl(
    private val infraConfig: InfraConfig,
) : TokenService {
    override fun generate(user: User): String {
        val expiration = getCurrentUtcDateTime().date.plus(1, DateTimeUnit.DAY)
        val javaInstant = expiration.atStartOfDayIn(TimeZone.UTC).toJavaInstant()
        val claims = UserClaims.toClaims(user)
        val jwtConfig = infraConfig.jwtConfig
        val token =
            JWT
                .create()
                .withAudience(jwtConfig.audience)
                .withIssuer(jwtConfig.issuer)
                .withPayload(Json.encodeToString(claims))
                .withExpiresAt(Date.from(javaInstant))
                .sign(jwtConfig.algorithm)

        return token
    }
}
