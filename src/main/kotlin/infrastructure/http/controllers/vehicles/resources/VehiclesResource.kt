package com.khrix.infrastructure.http.controllers.vehicles.resources

import io.ktor.resources.*

@Resource("/vehicles")
class VehiclesResource() {
    @Resource("create")
    class Create(val parent: VehiclesResource = VehiclesResource())
}