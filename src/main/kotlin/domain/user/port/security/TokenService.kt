package com.khrix.domain.user.port.security

import com.khrix.domain.user.model.User

interface TokenService {
    fun generate(user: User): String
}
