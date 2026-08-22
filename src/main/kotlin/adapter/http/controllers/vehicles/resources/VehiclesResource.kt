package com.khrix.adapter.http.controllers.vehicles.resources

import io.ktor.resources.Resource

@Resource("/vehicles")
class VehiclesResource {
    @Resource("create")
    class Create(
        val parent: VehiclesResource = VehiclesResource(),
    )

    @Resource("update")
    class Update(
        val parent: VehiclesResource = VehiclesResource(),
    )

    @Resource("delete/{id}")
    class Delete(
        val parent: VehiclesResource = VehiclesResource(),
        val id: String,
    )

    @Resource("owner")
    class Owner(
        val parent: VehiclesResource = VehiclesResource(),
    )
}
