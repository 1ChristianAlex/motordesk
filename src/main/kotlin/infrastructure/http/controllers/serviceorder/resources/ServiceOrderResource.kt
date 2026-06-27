package com.khrix.infrastructure.http.controllers.serviceorder.resources

import io.ktor.resources.*

@Resource("/service-order")
class ServiceOrderResource {
    @Resource("create")
    class Create(val parent: ServiceOrderResource = ServiceOrderResource())

    @Resource("update")
    class Update(val parent: ServiceOrderResource = ServiceOrderResource())

    @Resource("delete/{code}")
    class Delete(val parent: ServiceOrderResource = ServiceOrderResource(), val code: String)

    @Resource("list")
    class Owner(val parent: ServiceOrderResource = ServiceOrderResource())

    @Resource("code")
    class Code(val parent: ServiceOrderResource = ServiceOrderResource())
}