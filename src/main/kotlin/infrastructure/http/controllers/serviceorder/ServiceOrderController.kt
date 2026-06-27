package com.khrix.infrastructure.http.controllers.serviceorder

import com.khrix.domain.serviceorder.model.ServiceOrderStatus
import com.khrix.infrastructure.http.controllers.core.AppController
import com.khrix.infrastructure.http.controllers.core.getBody
import com.khrix.infrastructure.http.controllers.serviceorder.handlers.CreateNewServiceOrderHandler
import com.khrix.infrastructure.http.controllers.serviceorder.handlers.GetClientServiceOrderHandler
import com.khrix.infrastructure.http.controllers.serviceorder.handlers.UpdateServiceOrderHandler
import com.khrix.infrastructure.http.controllers.serviceorder.resources.ServiceOrderResource
import com.khrix.infrastructure.http.controllers.serviceorder.resources.dto.ServiceOrderInputDto
import com.khrix.infrastructure.http.controllers.serviceorder.resources.dto.UpdateServiceOrderInputDto
import com.khrix.infrastructure.security.UserClaims
import io.ktor.server.resources.delete
import io.ktor.server.resources.get
import io.ktor.server.resources.post
import io.ktor.server.routing.*

class ServiceOrderController(
    private val createNewServiceOrderHandler: CreateNewServiceOrderHandler,
    private val updateServiceOrderHandler: UpdateServiceOrderHandler,
    private val getClientServiceOrderHandler: GetClientServiceOrderHandler
) : AppController() {

    private suspend fun updateHandler(call: RoutingCall, body: UpdateServiceOrderInputDto) {
        val claims = UserClaims.getClaims(call)
        body.apply {
            setOperatorRole(claims.role)
        }
        call.send(updateServiceOrderHandler.handler(body))
    }

    override fun map(routing: Routing) {
        with(routing) {
            manager {
                post<ServiceOrderResource.Create> {
                    val body = getBody<ServiceOrderInputDto>()
                    call.send(createNewServiceOrderHandler.handler(body))
                }
                delete<ServiceOrderResource.Delete> {
                    updateHandler(
                        call = call,
                        body = UpdateServiceOrderInputDto(
                            code = it.code, status = ServiceOrderStatus.CANCELLED
                        )
                    )
                }
            }
            engineer {
                put<ServiceOrderResource.Update> {
                    val body = getBody<UpdateServiceOrderInputDto>()
                    updateHandler(
                        call = call,
                        body = body
                    )
                }
            }
            client {
                get<ServiceOrderResource.Client> {
                    val claims = UserClaims.getClaims(call)

                    call.send(getClientServiceOrderHandler.handler(claims.userId))
                }
            }
        }
    }
}
