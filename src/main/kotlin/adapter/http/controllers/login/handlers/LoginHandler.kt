package com.khrix.adapter.http.controllers.login.handlers

import com.khrix.adapter.http.controllers.core.HTTPHandler
import com.khrix.adapter.http.controllers.core.dto.AuthenticateOutputDto
import com.khrix.adapter.http.controllers.login.resources.dto.LoginInputDto

interface LoginHandler : HTTPHandler<LoginInputDto, AuthenticateOutputDto>
