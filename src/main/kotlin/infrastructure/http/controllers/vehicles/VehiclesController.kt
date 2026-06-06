package com.khrix.infrastructure.http.controllers.vehicles

import com.khrix.infrastructure.http.controllers.core.getBody
import com.khrix.infrastructure.http.controllers.vehicles.handlers.CreateNewVehicleHandler
import com.khrix.infrastructure.http.controllers.vehicles.handlers.DeleteVehicleHandler
import com.khrix.infrastructure.http.controllers.vehicles.handlers.GetVehicleByOwnerHandler
import com.khrix.infrastructure.http.controllers.vehicles.handlers.NewVehicleRequest
import com.khrix.infrastructure.http.controllers.vehicles.handlers.UpdateVehicleHandler
import com.khrix.infrastructure.http.controllers.vehicles.handlers.UpdateVehicleRequest
import com.khrix.infrastructure.http.controllers.vehicles.resources.VehiclesResource
import com.khrix.infrastructure.http.controllers.vehicles.resources.dto.VehicleInputDto
import com.khrix.infrastructure.http.core.AppController
import com.khrix.infrastructure.security.UserClaims
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.routing.*
import kotlinx.serialization.ExperimentalSerializationApi

class VehiclesController(
    private val createNewVehicleHandler: CreateNewVehicleHandler,
    private val updateVehicleHandler: UpdateVehicleHandler,
    private val deleteVehicleHandler: DeleteVehicleHandler,
    private val getVehicleByOwnerHandler: GetVehicleByOwnerHandler
) : AppController() {
    @OptIn(ExperimentalSerializationApi::class)
    override fun map(routing: Routing) {
        with(routing) {
            authenticate("auth-jwt") {
                post<VehiclesResource.Create> {
                    val body = getBody<VehicleInputDto>()
                    val claims = UserClaims.getClaims(call)
                    call.send(createNewVehicleHandler.handler(NewVehicleRequest(claims, body)))
                }
                post<VehiclesResource.Update> {
                    val body = getBody<VehicleInputDto>()

                    val claims = UserClaims.getClaims(call)
                    call.send(updateVehicleHandler.handler(UpdateVehicleRequest(body, claims.userId)))
                }
                get<VehiclesResource.Owner> {
                    val claims = UserClaims.getClaims(call)
                    call.send(getVehicleByOwnerHandler.handler(claims.userId))
                }
                delete<VehiclesResource.Delete> {
                    call.send(deleteVehicleHandler.handler(it.id.toInt()))
                }
            }
        }
    }
}
