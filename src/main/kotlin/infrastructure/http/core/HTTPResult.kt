package com.khrix.infrastructure.http.core

import com.khrix.infrastructure.http.serializers.HttpStatusCodeSerializer
import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
data class HTTPResult<Data>(
    val data: Data?,
    @Serializable(with = HttpStatusCodeSerializer::class)
    val status: HttpStatusCode = HttpStatusCode.OK,
    val message: String? = null
)