package com.khrix.domain.address.core

interface BaseMapper<Input, Output> {
    fun fromModel(data: Output): Input
    fun toModel(data: Input): Output
}