package com.khrix.adapter.inbound.http.controllers.login.resources.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginInputDto(
    val userName: String,
    val password: String,
)
