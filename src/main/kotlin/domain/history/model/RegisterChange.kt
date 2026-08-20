package com.khrix.domain.history.model

data class RegisterChange<T : Comparable<String>>(
    val propertyName: String,
    val value: T,
)
