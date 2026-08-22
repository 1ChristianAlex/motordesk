package com.khrix.adapter.inbound.http.controllers.user.handlers

import com.khrix.adapter.inbound.http.controllers.core.HTTPHandler
import com.khrix.adapter.inbound.http.controllers.user.resources.dto.UserOutputDto
import com.khrix.adapter.security.UserClaims

interface GetSelfUserHandler : HTTPHandler<UserClaims, UserOutputDto>
