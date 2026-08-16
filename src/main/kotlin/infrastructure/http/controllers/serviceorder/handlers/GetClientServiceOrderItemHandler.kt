package com.khrix.infrastructure.http.controllers.serviceorder.handlers

import com.khrix.infrastructure.http.controllers.core.HTTPHandler
import com.khrix.infrastructure.http.controllers.serviceorder.resources.dto.ClientServiceOrderItemInputDto
import com.khrix.infrastructure.http.controllers.serviceorder.resources.dto.ServiceOrderOutputDto

interface GetClientServiceOrderItemHandler : HTTPHandler<ClientServiceOrderItemInputDto, ServiceOrderOutputDto>
