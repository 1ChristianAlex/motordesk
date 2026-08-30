package com.khrix.adapter.inbound.http.controllers.login.handlers

import com.khrix.adapter.inbound.http.controllers.core.HTTPHandler
import com.khrix.adapter.inbound.http.controllers.core.dto.AuthenticateOutputDto
import com.khrix.adapter.inbound.http.controllers.login.resources.dto.LoginInputDto

interface LoginHandler : HTTPHandler<LoginInputDto, AuthenticateOutputDto>
