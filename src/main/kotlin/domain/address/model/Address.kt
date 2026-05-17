package com.khrix.domain.address.model

import kotlinx.datetime.LocalDateTime

data class Address(
    val id: Int? = null,
    val street: String,
    val number: String,
    val complement: String? = null,
    val neighborhood: String,
    val city: String,
    val state: String,
    val country: String,
    val zipCode: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
