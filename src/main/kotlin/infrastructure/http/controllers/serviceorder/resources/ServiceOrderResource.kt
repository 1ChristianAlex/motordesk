package com.khrix.infrastructure.http.controllers.serviceorder.resources

import io.ktor.resources.Resource

@Resource("/service-order")
class ServiceOrderResource {
    @Resource("manager")
    class Manager(
        val parent: ServiceOrderResource = ServiceOrderResource(),
    ) {
        @Resource("create")
        class Create(
            val parent: Manager = Manager(),
        )

        @Resource("update")
        class Update(
            val parent: Manager = Manager(),
        )

        @Resource("{code}/delete")
        class Delete(
            val parent: Manager = Manager(),
            val code: String,
        )

        @Resource("list")
        class Owner(
            val parent: Manager = Manager(),
        )

        @Resource("{code}")
        class Code(
            val parent: Manager = Manager(),
            val code: String,
        )
    }

    @Resource("client")
    class Client(
        val parent: ServiceOrderResource = ServiceOrderResource(),
    ) {
        @Resource("{code}")
        class Individual(
            val parent: Client = Client(),
            val code: String,
        ) {
            @Resource("approval")
            class Approval(
                val parent: Individual,
                val token: String,
            )
        }
    }
}
