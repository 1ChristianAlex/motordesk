package com.khrix.infrastructure.http.controllers.user.handlers

import com.khrix.infrastructure.http.controllers.core.dto.AuthenticateOutputDto
import com.khrix.infrastructure.http.controllers.user.resources.dto.UserInputDto
import com.khrix.infrastructure.http.core.HTTPHandler

interface UpdateSelfUserHandler : HTTPHandler<UserInputDto, AuthenticateOutputDto>