package com.khrix.infrastructure.http.controllers.core.dto

import com.khrix.infrastructure.http.controllers.user.resources.dto.UserOutputDto
import kotlinx.serialization.Serializable

@Serializable
data class AuthenticateOutputDto(
    val token: String,
    val user: UserOutputDto
) {
}