package com.khrix.infrastructure.http.controllers.login.resources.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginDto(val userName: String, val password: String)
