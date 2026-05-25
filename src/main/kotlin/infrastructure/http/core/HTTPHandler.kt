package com.khrix.infrastructure.http.core

import com.khrix.domain.valueobject.ValidationErrorResult
import io.ktor.http.*

interface HTTPHandler<Body, Output> {
    suspend fun handler(body: Body): HttpResult<Output>
}

abstract class BaseHTTPHandler<Body, Output> : HTTPHandler<Body, Output> {
    override suspend fun handler(body: Body): HttpResult<Output> {
        return try {
            handle(body)
        } catch (error: ValidationErrorResult) {
            HttpResult(null, HttpStatusCode.BadRequest, error.validationErrors)
        } catch (exception: Exception) {
            HttpResult(
                null,
                HttpStatusCode.BadRequest,
                listOf(exception.message ?: "An error occurred")
            )
        }
    }

    protected abstract suspend fun handle(body: Body): HttpResult<Output>
}