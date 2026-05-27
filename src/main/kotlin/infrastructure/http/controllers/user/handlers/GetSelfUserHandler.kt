package com.khrix.infrastructure.http.controllers.user.handlers

import com.khrix.infrastructure.http.controllers.user.resources.dto.UserOutputDto
import com.khrix.infrastructure.http.core.HTTPHandler
import com.khrix.infrastructure.security.UserClaims

interface GetSelfUserHandler : HTTPHandler<UserClaims, UserOutputDto>