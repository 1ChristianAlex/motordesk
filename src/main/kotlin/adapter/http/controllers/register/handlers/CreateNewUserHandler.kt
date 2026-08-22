package com.khrix.adapter.http.controllers.register.handlers

import com.khrix.adapter.http.controllers.core.HTTPHandler
import com.khrix.adapter.http.controllers.core.dto.AuthenticateOutputDto
import com.khrix.adapter.http.controllers.register.resources.dto.ClientRegisterDto
import com.khrix.domain.user.model.Role

data class CreateNewUserRequest(
    val clientRegisterDto: ClientRegisterDto,
) {
    fun updateRole(role: Role) {
        clientRegisterDto.user.role = role
    }
}

interface CreateNewUserHandler : HTTPHandler<CreateNewUserRequest, AuthenticateOutputDto>
