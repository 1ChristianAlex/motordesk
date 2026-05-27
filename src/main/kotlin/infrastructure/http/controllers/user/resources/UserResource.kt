package com.khrix.infrastructure.http.controllers.user.resources

import io.ktor.resources.*

@Resource("/self")
class UserResource() {
    @Resource("update")
    class Update(val parent: UserResource = UserResource())
}