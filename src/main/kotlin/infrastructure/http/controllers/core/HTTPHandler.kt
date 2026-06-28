package com.khrix.infrastructure.http.controllers.core

import com.khrix.domain.valueobject.ValidationErrorResult
import com.khrix.infrastructure.http.controllers.core.exceptions.HandlerException
import io.ktor.openapi.*

interface HTTPHandler<Body, Output> {
    suspend fun handler(body: Body): HttpResult<Output>
    fun description(
        configure: Operation.Builder
    ): Unit
}

abstract class BaseHTTPHandler<Body, Output> : HTTPHandler<Body, Output> {
    override suspend fun handler(body: Body): HttpResult<Output> {
        return try {
            handle(body)
        } catch (error: ValidationErrorResult) {
            HandlerException.fromValidationErrorResult(error)
        } catch (exception: Exception) {
            HandlerException.toHttpResultError(HandlerException.BadRequest(exception))
        }
    }

    protected abstract suspend fun handle(body: Body): HttpResult<Output>

    abstract override fun description(configure: Operation.Builder)
}