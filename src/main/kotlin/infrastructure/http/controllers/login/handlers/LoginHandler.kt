package com.khrix.infrastructure.http.controllers.login.handlers

import com.khrix.infrastructure.http.controllers.core.dto.AuthenticateOutputDto
import com.khrix.infrastructure.http.controllers.login.resources.dto.LoginDto
import com.khrix.infrastructure.http.core.HTTPHandler

interface LoginHandler : HTTPHandler<LoginDto, AuthenticateOutputDto>