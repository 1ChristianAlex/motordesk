package com.khrix.infrastructure.http.controllers.serviceorder.resources

import io.ktor.resources.*

@Resource("/service-order")
class ServiceOrderResource {
    @Resource("create")
    class Create(val parent: ServiceOrderResource = ServiceOrderResource())

    @Resource("update")
    class Update(val parent: ServiceOrderResource = ServiceOrderResource())

    @Resource("delete/{id}")
    class Delete(val parent: ServiceOrderResource = ServiceOrderResource(), val id: String)

    @Resource("list")
    class Owner(val parent: ServiceOrderResource = ServiceOrderResource())

    @Resource("byId")
    class ById(val parent: ServiceOrderResource = ServiceOrderResource())
}