package com.khrix.domain.user.security

import com.khrix.domain.user.model.User

interface TokenService {
    fun generate(
        user: User
    ): String
}