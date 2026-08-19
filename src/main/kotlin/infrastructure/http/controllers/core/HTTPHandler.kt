package com.khrix.infrastructure.http.controllers.core

import com.khrix.domain.valueobject.ValidationErrorResult
import com.khrix.infrastructure.http.controllers.core.exceptions.HandlerException
import io.ktor.openapi.Operation
import org.slf4j.LoggerFactory

interface HTTPHandler<Body, Output> {
    suspend fun handler(body: Body): HttpResult<Output>

    fun description(configure: Operation.Builder)
}

abstract class BaseHTTPHandler<Body, Output> : HTTPHandler<Body, Output> {
    private val logger = LoggerFactory.getLogger(javaClass)

    override suspend fun handler(body: Body): HttpResult<Output> =
        try {
            logger.info("Executing controller handler")
            handle(body)
        } catch (error: ValidationErrorResult) {
            logger.error(error.message, error.cause)
            HandlerException.fromValidationErrorResult(error)
        } catch (exception: Exception) {
            logger.error(exception.message, exception.cause)
            HandlerException.toHttpResultError(HandlerException.BadRequest(exception))
        }

    protected abstract suspend fun handle(body: Body): HttpResult<Output>

    abstract override fun description(configure: Operation.Builder)
}
