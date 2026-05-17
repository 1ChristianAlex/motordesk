package com.khrix.domain.core

interface BaseMapper<Input, Output> {
    fun fromModel(data: Output): Input
    fun toModel(data: Input): Output
}