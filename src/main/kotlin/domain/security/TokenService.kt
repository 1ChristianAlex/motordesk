package com.khrix.domain.security

import com.khrix.domain.user.model.User

interface TokenService {
    fun generate(
        user: User
    ): String
}