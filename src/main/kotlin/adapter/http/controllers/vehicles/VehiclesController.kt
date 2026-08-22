package com.khrix.adapter.http.controllers.vehicles

import com.khrix.adapter.http.controllers.core.AppController
import com.khrix.adapter.http.controllers.core.getBody
import com.khrix.adapter.http.controllers.vehicles.handlers.CreateNewVehicleHandler
import com.khrix.adapter.http.controllers.vehicles.handlers.DeleteVehicleHandler
import com.khrix.adapter.http.controllers.vehicles.handlers.GetVehicleByOwnerHandler
import com.khrix.adapter.http.controllers.vehicles.handlers.UpdateVehicleHandler
import com.khrix.adapter.http.controllers.vehicles.resources.VehiclesResource
import com.khrix.adapter.http.controllers.vehicles.resources.dto.VehicleInputDto
import com.khrix.adapter.http.controllers.vehicles.resources.dto.VehicleUpdateInputDto
import com.khrix.adapter.security.UserClaims
import io.ktor.server.resources.delete
import io.ktor.server.resources.get
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.routing.Routing
import io.ktor.server.routing.openapi.describe

class VehiclesController(
    private val createNewVehicleHandler: CreateNewVehicleHandler,
    private val updateVehicleHandler: UpdateVehicleHandler,
    private val deleteVehicleHandler: DeleteVehicleHandler,
    private val getVehicleByOwnerHandler: GetVehicleByOwnerHandler,
) : AppController() {
    override fun map(routing: Routing) {
        with(routing) {
            client {
                get<VehiclesResource.Owner> {
                    val claims = UserClaims.getClaims(call)
                    call.send(getVehicleByOwnerHandler.handler(claims.userId))
                }.describe(getVehicleByOwnerHandler::description)
            }
            manager {
                post<VehiclesResource.Create> {
                    val body = getBody<VehicleInputDto>()

                    call.send(createNewVehicleHandler.handler(body))
                }.describe(createNewVehicleHandler::description)
                put<VehiclesResource.Update> {
                    val body = getBody<VehicleUpdateInputDto>()
                    call.send(updateVehicleHandler.handler(body))
                }.describe(updateVehicleHandler::description)
                delete<VehiclesResource.Delete> {
                    call.send(deleteVehicleHandler.handler(it.id.toInt()))
                }.describe(deleteVehicleHandler::description)
            }
        }
    }
}
