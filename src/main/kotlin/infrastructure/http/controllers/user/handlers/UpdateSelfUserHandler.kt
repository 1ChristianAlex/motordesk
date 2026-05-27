package com.khrix.infrastructure.http.controllers.user.handlers

import com.khrix.infrastructure.http.controllers.core.dto.AuthenticateOutputDto
import com.khrix.infrastructure.http.controllers.user.resources.dto.UserInputDto
import com.khrix.infrastructure.http.core.HTTPHandler
import com.khrix.infrastructure.security.UserClaims

data class UpdateSelfUserHandlerBody(
    val user: UserInputDto,
    val claims: UserClaims
)

interface UpdateSelfUserHandler : HTTPHandler<UpdateSelfUserHandlerBody, AuthenticateOutputDto>
