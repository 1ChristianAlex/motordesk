package com.khrix.infrastructure.http.controllers.core.exceptions

import com.khrix.domain.valueobject.ValidationErrorResult
import com.khrix.infrastructure.http.core.HttpResult
import io.ktor.http.*

sealed class HandlerException(val statusCode: HttpStatusCode, override val message: String?) : Exception(message) {

    class UnauthenticatedOperation() :
        HandlerException(HttpStatusCode.Unauthorized, "Unauthenticated operation. Please provide valid credentials.")

    class InvalidPermissionOperation() :
        HandlerException(
            HttpStatusCode.Forbidden,
            "Invalid permission. You do not have the necessary permissions to perform this operation."
        )

    class BadRequest(exception: Throwable) :
        HandlerException(
            HttpStatusCode.BadRequest,
            exception.message ?: "Invalid request. Please check your input and try again."
        )

    companion object {
        fun <Body> toHttpResultError(handlerException: HandlerException): HttpResult<Body> {
            return HttpResult(null, handlerException.statusCode, listOf(handlerException.message!!))
        }

        fun <Body> fromValidationErrorResult(value: ValidationErrorResult): HttpResult<Body> {
            return HttpResult(null, HttpStatusCode.BadRequest, value.validationErrors)
        }
    }
}