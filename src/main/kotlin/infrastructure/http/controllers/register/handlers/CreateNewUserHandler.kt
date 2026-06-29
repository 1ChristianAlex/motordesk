package com.khrix.infrastructure.http.controllers.register.handlers

import com.khrix.domain.user.model.Role
import com.khrix.infrastructure.http.controllers.core.HTTPHandler
import com.khrix.infrastructure.http.controllers.core.dto.AuthenticateOutputDto
import com.khrix.infrastructure.http.controllers.register.resources.dto.ClientRegisterDto

data class CreateNewUserRequest(
    val clientRegisterDto: ClientRegisterDto,
) {
    fun updateRole(role: Role) {
        clientRegisterDto.user.role = role
    }
}

interface CreateNewUserHandler : HTTPHandler<CreateNewUserRequest, AuthenticateOutputDto>
