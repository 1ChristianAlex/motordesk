package com.khrix.adapter.inbound.http.controllers.register.resources

import io.ktor.resources.Resource

@Resource("/register")
class RegisterResource {
    @Resource("manager")
    class Manager(
        val parent: RegisterResource = RegisterResource(),
    )

    @Resource("client")
    class Client(
        val parent: RegisterResource = RegisterResource(),
    )
}
