package com.khrix.infrastructure.http.controllers.register.handlers

import com.khrix.domain.user.model.Role
import com.khrix.infrastructure.http.controllers.core.dto.AuthenticateOutputDto
import com.khrix.infrastructure.http.controllers.register.resources.dto.ClientRegisterDto
import com.khrix.infrastructure.http.controllers.core.HTTPHandler

data class CreateNewUserRequest(val clientRegisterDto: ClientRegisterDto) {
    private var role: Role = Role.CLIENT

    fun updateRole(role: Role) {
        this.role = role
    }
}

interface CreateNewUserHandler : HTTPHandler<CreateNewUserRequest, AuthenticateOutputDto>