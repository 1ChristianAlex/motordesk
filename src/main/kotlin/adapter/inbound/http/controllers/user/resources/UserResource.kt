package com.khrix.adapter.inbound.http.controllers.user.resources

import io.ktor.resources.Resource

@Resource("/self")
class UserResource {
    @Resource("update")
    class Update(
        val parent: UserResource = UserResource(),
    )
}
