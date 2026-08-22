package com.khrix.adapter.http.controllers.core.exceptions

import com.khrix.adapter.http.controllers.core.HttpResult
import com.khrix.domain.valueobject.ValidationErrorResult
import io.ktor.http.HttpStatusCode

sealed class HandlerException(
    val statusCode: HttpStatusCode,
    override val message: String?,
) : Exception(message) {
    class UnauthenticatedOperation :
        HandlerException(HttpStatusCode.Unauthorized, "Unauthenticated operation. Please provide valid credentials.")

    class InvalidPermissionOperation :
        HandlerException(
            HttpStatusCode.Forbidden,
            "Invalid permission. You do not have the necessary permissions to perform this operation.",
        )

    class BadRequest : HandlerException {
        constructor(exception: Throwable) : super(
            HttpStatusCode.BadRequest,
            exception.message ?: "Invalid request. Please check your input and try again.",
        )

        constructor(message: String) : super(
            HttpStatusCode.BadRequest,
            message,
        )
    }

    companion object {
        fun <Body> toHttpResultError(handlerException: HandlerException): HttpResult<Body> =
            HttpResult(null, handlerException.statusCode, listOf(handlerException.message!!))

        fun <Body> fromValidationErrorResult(value: ValidationErrorResult): HttpResult<Body> =
            HttpResult(null, HttpStatusCode.BadRequest, value.validationErrors)
    }
}
