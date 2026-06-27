package com.khrix.infrastructure.http.controllers.serviceorder.handlers

import com.khrix.infrastructure.http.controllers.core.HTTPHandler
import com.khrix.infrastructure.http.controllers.serviceorder.resources.dto.ServiceOrderOutputDto

interface GetClientServiceOrderHandler : HTTPHandler<Int, List<ServiceOrderOutputDto>>