package com.khrix.adapter.http.controllers.core.dto

import com.khrix.adapter.http.controllers.user.resources.dto.UserOutputDto
import kotlinx.serialization.Serializable

@Serializable
data class AuthenticateOutputDto(
    val token: String,
    val user: UserOutputDto,
)
