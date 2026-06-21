package com.khrix.infrastructure.http.controllers.serviceorder

import com.khrix.infrastructure.http.controllers.core.AppController
import com.khrix.infrastructure.http.controllers.core.getBody
import com.khrix.infrastructure.http.controllers.serviceorder.handlers.CreateNewServiceOrder
import com.khrix.infrastructure.http.controllers.serviceorder.resources.ServiceOrderResource
import com.khrix.infrastructure.http.controllers.serviceorder.resources.dto.ServiceOrderInputDto
import io.ktor.server.resources.post
import io.ktor.server.routing.*
import kotlinx.serialization.ExperimentalSerializationApi

class ServiceOrderController(
    private val createNewServiceOrder: CreateNewServiceOrder,
) : AppController() {
    @OptIn(ExperimentalSerializationApi::class)
    override fun map(routing: Routing) {
        with(routing) {
            manager {
                post<ServiceOrderResource.Create> {
                    val body = getBody<ServiceOrderInputDto>()
                    call.send(createNewServiceOrder.handler(body))
                }
            }
        }
    }
}
