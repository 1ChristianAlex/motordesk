package com.khrix.infrastructure.http.register.resources.dto

import kotlinx.serialization.Serializable

@Serializable
data class NewClientDto(
    val name: String,
    val lastname: String,
    val email: String,
    val phone: String,
    val password: String,
    val cpf: String,
    val address: AddressDto,
)

@Serializable
data class AddressDto(
    val street: String,
    val number: String,
    val complement: String? = null,
    val neighborhood: String,
    val city: String,
    val state: String,
    val country: String,
    val zipCode: String,
)