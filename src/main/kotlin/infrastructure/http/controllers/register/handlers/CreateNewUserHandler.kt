package com.khrix.infrastructure.http.controllers.register.handlers

import com.khrix.infrastructure.http.controllers.register.resources.dto.ClientRegisterDto
import com.khrix.infrastructure.http.controllers.user.resources.dto.UserOutputDto
import com.khrix.infrastructure.http.core.HTTPHandler

interface CreateNewUserHandler : HTTPHandler<ClientRegisterDto, UserOutputDto>