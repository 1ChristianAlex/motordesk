package com.khrix.infrastructure.http.controllers.serviceorder.handlers

import com.khrix.infrastructure.http.controllers.core.HTTPHandler
import com.khrix.infrastructure.http.controllers.serviceorder.resources.dto.ServiceOrderOutputDto
import com.khrix.infrastructure.http.controllers.serviceorder.resources.dto.UpdateServiceOrderInputDto


interface UpdateServiceOrderHandler : HTTPHandler<UpdateServiceOrderInputDto, ServiceOrderOutputDto>