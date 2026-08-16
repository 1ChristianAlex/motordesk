package com.khrix.infrastructure.http.controllers.login.handlers

import com.khrix.infrastructure.http.controllers.core.HTTPHandler
import com.khrix.infrastructure.http.controllers.core.dto.AuthenticateOutputDto
import com.khrix.infrastructure.http.controllers.login.resources.dto.LoginInputDto

interface LoginHandler : HTTPHandler<LoginInputDto, AuthenticateOutputDto>
