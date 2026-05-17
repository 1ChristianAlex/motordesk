package com.khrix.domain.user.model

data class User(
    val id: Int,
    val addressId: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val phone: String?,
    val cpf: String?,
    val isActive: Boolean,
)