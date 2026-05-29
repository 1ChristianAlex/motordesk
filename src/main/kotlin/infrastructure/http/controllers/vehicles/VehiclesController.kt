package com.khrix.infrastructure.http.controllers.vehicles

import com.khrix.infrastructure.http.controllers.core.getBody
import com.khrix.infrastructure.http.controllers.vehicles.handlers.CreateNewVehicleHandler
import com.khrix.infrastructure.http.controllers.vehicles.handlers.NewVehicleRequest
import com.khrix.infrastructure.http.controllers.vehicles.resources.VehiclesResource
import com.khrix.infrastructure.http.controllers.vehicles.resources.dto.VehicleInputDto
import com.khrix.infrastructure.http.core.AppController
import com.khrix.infrastructure.security.UserClaims
import io.ktor.server.auth.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.ExperimentalSerializationApi

class VehiclesController(
    private val createNewVehicleHandler: CreateNewVehicleHandler
) : AppController {
    @OptIn(ExperimentalSerializationApi::class)
    override fun map(routing: Routing) {
        with(routing) {
            authenticate("auth-jwt") {
                post<VehiclesResource.Create> {
                    val body = getBody<VehicleInputDto>()
                    val claims = UserClaims.getClaims(call)
                    call.respond(createNewVehicleHandler.handler(NewVehicleRequest(claims, body)))
                }
            }
        }
    }
}
