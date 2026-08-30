package com.khrix.adapter.inbound.http.controllers.core

import com.khrix.adapter.inbound.http.serializers.HttpStatusCodeSerializer
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.Serializable

@Serializable
data class HttpResult<Data>(
    val data: Data?,
    @Serializable(with = HttpStatusCodeSerializer::class)
    val status: HttpStatusCode = HttpStatusCode.OK,
    val errors: List<String>? = null,
)
