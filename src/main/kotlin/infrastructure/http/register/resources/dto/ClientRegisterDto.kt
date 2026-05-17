package com.khrix.infrastructure.http.register.resources.dto

import kotlinx.datetime.LocalDate
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
    val binaryRefAddr: LocalDate
)

@Serializable
data class AddressDto(
    val street: String,
    val city: String,
    val zipcode: String,
    val streetNumber: String,
)