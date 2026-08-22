package com.khrix.adapter.inbound.http.controllers.user.handlers

import com.khrix.adapter.inbound.http.controllers.core.HTTPHandler
import com.khrix.adapter.inbound.http.controllers.core.dto.AuthenticateOutputDto
import com.khrix.adapter.inbound.http.controllers.user.resources.dto.UserInputDto
import com.khrix.adapter.security.UserClaims

data class UpdateSelfUserHandlerBody(
    val user: UserInputDto,
    val claims: UserClaims,
)

interface UpdateSelfUserHandler : HTTPHandler<UpdateSelfUserHandlerBody, AuthenticateOutputDto>
