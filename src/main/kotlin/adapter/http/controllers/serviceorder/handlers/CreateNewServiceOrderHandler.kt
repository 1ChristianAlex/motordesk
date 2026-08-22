package com.khrix.adapter.http.controllers.serviceorder.handlers

import com.khrix.adapter.http.controllers.core.HTTPHandler
import com.khrix.adapter.http.controllers.serviceorder.resources.dto.ServiceOrderInputDto
import com.khrix.adapter.http.controllers.serviceorder.resources.dto.ServiceOrderOutputDto

interface CreateNewServiceOrderHandler : HTTPHandler<ServiceOrderInputDto, ServiceOrderOutputDto>
