package com.khrix.infrastructure.security

import com.auth0.jwt.interfaces.Payload
import com.khrix.domain.user.model.Role
import com.khrix.domain.user.model.User
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.routing.RoutingCall
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserClaims(
    @SerialName("firstName") val firstName: String,
    @SerialName("lastName") val lastName: String,
    @SerialName("email") val email: String,
    @SerialName("cpf") val cpf: String,
    @SerialName("userId") val userId: Int,
    @SerialName("role") val role: Role,
) {
    companion object {
        fun getClaims(payload: Payload): UserClaims {
            val firstName = payload.getClaim("firstName").asString()
            val lastName = payload.getClaim("lastName").asString()
            val email = payload.getClaim("email").asString()
            val cpf = payload.getClaim("cpf").asString()
            val userId = payload.getClaim("userId").asInt()
            val role = payload.getClaim("role").asString()

            return UserClaims(
                firstName = firstName,
                lastName = lastName,
                email = email,
                cpf = cpf,
                userId = userId,
                role = Role.valueOf(role),
            )
        }

        fun getClaims(call: RoutingCall): UserClaims {
            val principal = call.principal<JWTPrincipal>()
            return principal?.let {
                val firstName = it.payload.getClaim("firstName").asString()
                val lastName = it.payload.getClaim("lastName").asString()
                val email = it.payload.getClaim("email").asString()
                val cpf = it.payload.getClaim("cpf").asString()
                val userId = it.payload.getClaim("userId").asInt()
                val role = it.payload.getClaim("role").asString()

                UserClaims(
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    cpf = cpf,
                    userId = userId,
                    role = Role.valueOf(role),
                )
            } ?: throw NoSuchElementException("User claims not found")
        }

        fun toClaims(user: User): UserClaims =
            UserClaims(
                firstName = user.firstName.value,
                lastName = user.lastName.value,
                email = user.email.value,
                cpf = user.cpf.value,
                userId = user.id,
                role = user.role,
            )
    }
}
