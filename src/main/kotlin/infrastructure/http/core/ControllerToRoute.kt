package com.khrix.infrastructure.http.core

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import io.ktor.utils.io.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import kotlin.reflect.KClass

@OptIn(ExperimentalSerializationApi::class, InternalAPI::class)
class ControllerToRoute {
    suspend fun <Data> toRoute(
        handler: HTTPHandler<Data, *>,
        params: Data,
        call: RoutingCall
    ) {
        try {
            val result = handler.handler(params)
            handleSuccess(call, result)

        } catch (ex: Exception) {
            handleError(call, ex)
        }
    }

    private fun getRootCause(ex: Exception): Exception {
        return (ex.rootCause ?: ex) as Exception
    }

    private suspend fun handleError(call: RoutingCall, ex: Exception) {
        val root = getRootCause(ex)
        call.apply {
            response.status(HttpStatusCode.BadRequest)

            when (root) {
                is MissingFieldException -> {
                    respond(
                        HttpResult(
                            root.missingFields.joinToString(";") + " are required",
                            HttpStatusCode.BadRequest
                        )
                    )
                }

                else -> {
                    respond(HttpResult(ex.message, HttpStatusCode.BadRequest))
                }
            }
        }
    }

    suspend fun <Body : Any> toRoute(
        handler: HTTPHandler<Body, *>,
        body: KClass<Body>,
        call: RoutingCall
    ) {
        try {
            val productData = call.receive(body)
            val result = handler.handler(productData)
            handleSuccess(call, result)

        } catch (ex: Exception) {
            handleError(call, ex)
        }
    }

    private suspend fun handleSuccess(
        call: RoutingCall,
        result: HttpResult<out Any?>
    ) {
        call.apply {
            if (HttpStatusCode.NoContent == result.status) {
                respond(result.status)
            } else {
                response.status(result.status)
                result.data?.let {
                    respond(it)
                }
            }
        }
    }
}