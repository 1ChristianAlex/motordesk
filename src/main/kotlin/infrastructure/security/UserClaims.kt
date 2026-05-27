package com.khrix.infrastructure.security

import com.khrix.domain.user.model.User
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserClaims(
    @SerialName("firstName") val firstName: String,
    @SerialName("lastName") val lastName: String,
    @SerialName("email") val email: String,
    @SerialName("cpf") val cpf: String,
    @SerialName("userId") val userId: Int,
) {
    companion object {
        fun getClaims(call: RoutingCall): UserClaims {
            val principal = call.principal<JWTPrincipal>()
            return principal?.let {
                val firstName = it.payload.getClaim("firstName").asString()
                val lastName = it.payload.getClaim("lastName").asString()
                val email = it.payload.getClaim("email").asString()
                val cpf = it.payload.getClaim("cpf").asString()
                val userId = it.payload.getClaim("userId").asInt()

                UserClaims(
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    cpf = cpf,
                    userId = userId
                )
            } ?: throw Exception("User claims not found")
        }

        fun toClaims(user: User): UserClaims {
            return UserClaims(
                firstName = user.firstName.value,
                lastName = user.lastName.value,
                email = user.email.value,
                cpf = user.cpf.value,
                userId = user.id
            )
        }
    }
}

