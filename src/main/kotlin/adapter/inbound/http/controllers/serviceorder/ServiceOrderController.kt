package com.khrix.adapter.inbound.http.controllers.serviceorder

import com.khrix.adapter.inbound.http.controllers.core.AppController
import com.khrix.adapter.inbound.http.controllers.core.getBody
import com.khrix.adapter.inbound.http.controllers.serviceorder.handlers.ApprovesServiceOrderHandler
import com.khrix.adapter.inbound.http.controllers.serviceorder.handlers.CreateNewServiceOrderHandler
import com.khrix.adapter.inbound.http.controllers.serviceorder.handlers.GetClientServiceOrderItemHandler
import com.khrix.adapter.inbound.http.controllers.serviceorder.handlers.GetClientServicesOrderHandler
import com.khrix.adapter.inbound.http.controllers.serviceorder.handlers.GetServiceOrderItemHandler
import com.khrix.adapter.inbound.http.controllers.serviceorder.handlers.UpdateServiceOrderHandler
import com.khrix.adapter.inbound.http.controllers.serviceorder.resources.ServiceOrderResource
import com.khrix.adapter.inbound.http.controllers.serviceorder.resources.dto.ApprovesServiceOrderInputDto
import com.khrix.adapter.inbound.http.controllers.serviceorder.resources.dto.ClientServiceOrderItemInputDto
import com.khrix.adapter.inbound.http.controllers.serviceorder.resources.dto.ServiceOrderInputDto
import com.khrix.adapter.inbound.http.controllers.serviceorder.resources.dto.UpdateServiceOrderInputDto
import com.khrix.adapter.security.UserClaims
import com.khrix.domain.serviceorder.model.ServiceOrderStatus
import io.ktor.server.resources.delete
import io.ktor.server.resources.get
import io.ktor.server.resources.post
import io.ktor.server.routing.Routing
import io.ktor.server.routing.RoutingCall
import io.ktor.server.routing.openapi.describe
import io.ktor.server.routing.put

class ServiceOrderController(
    private val createNewServiceOrderHandler: CreateNewServiceOrderHandler,
    private val updateServiceOrderHandler: UpdateServiceOrderHandler,
    private val getClientServicesOrderHandler: GetClientServicesOrderHandler,
    private val getClientServiceOrderItemHandler: GetClientServiceOrderItemHandler,
    private val getServiceOrderItemHandler: GetServiceOrderItemHandler,
    private val approvesServiceOrderHandler: ApprovesServiceOrderHandler,
) : AppController() {
    private suspend fun updateHandler(
        call: RoutingCall,
        body: UpdateServiceOrderInputDto,
    ) {
        val claims = UserClaims.getClaims(call)
        body.apply {
            setOperatorRole(claims.role)
        }
        call.send(updateServiceOrderHandler.handler(body))
    }

    override fun map(routing: Routing) {
        with(routing) {
            manager {
                post<ServiceOrderResource.Manager.Create> {
                    val body = getBody<ServiceOrderInputDto>()
                    call.send(createNewServiceOrderHandler.handler(body))
                }.describe(createNewServiceOrderHandler::description)

                get<ServiceOrderResource.Manager.Code> {
                    call.send(getServiceOrderItemHandler.handler(it.code))
                }.describe(getServiceOrderItemHandler::description)

                delete<ServiceOrderResource.Manager.Delete> {
                    updateHandler(
                        call = call,
                        body =
                            UpdateServiceOrderInputDto(
                                code = it.code,
                                status = ServiceOrderStatus.CANCELLED,
                            ),
                    )
                }
            }
            engineer {
                put<ServiceOrderResource.Manager.Update> {
                    val body = getBody<UpdateServiceOrderInputDto>()
                    updateHandler(
                        call = call,
                        body = body,
                    )
                }.describe(updateServiceOrderHandler::description)
            }
            client {
                get<ServiceOrderResource.Client> {
                    val claims = UserClaims.getClaims(call)
                    call.send(getClientServicesOrderHandler.handler(claims.userId))
                }.describe(getClientServicesOrderHandler::description)
                get<ServiceOrderResource.Client.Individual> {
                    val claims = UserClaims.getClaims(call)
                    call.send(
                        getClientServiceOrderItemHandler.handler(
                            ClientServiceOrderItemInputDto(claims.userId, it.code),
                        ),
                    )
                }.describe(getClientServiceOrderItemHandler::description)
            }
            get<ServiceOrderResource.Client.Individual.Approval> {
                call.send(
                    approvesServiceOrderHandler.handler(ApprovesServiceOrderInputDto(it.token, it.parent.code)),
                )
            }.describe(approvesServiceOrderHandler::description)
        }
    }
}
