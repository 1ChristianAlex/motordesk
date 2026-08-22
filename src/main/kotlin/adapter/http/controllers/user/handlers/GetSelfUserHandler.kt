package com.khrix.adapter.http.controllers.user.handlers

import com.khrix.adapter.http.controllers.core.HTTPHandler
import com.khrix.adapter.http.controllers.user.resources.dto.UserOutputDto
import com.khrix.adapter.security.UserClaims

interface GetSelfUserHandler : HTTPHandler<UserClaims, UserOutputDto>
