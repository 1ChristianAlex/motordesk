package com.khrix.adapter.inbound.http.controllers.serviceorder.handlers

import com.khrix.adapter.inbound.http.controllers.core.HTTPHandler
import com.khrix.adapter.inbound.http.controllers.serviceorder.resources.dto.ServiceOrderWithHistoryOutputDto
import com.khrix.adapter.inbound.http.controllers.serviceorder.resources.dto.UpdateServiceOrderInputDto

interface UpdateServiceOrderHandler : HTTPHandler<UpdateServiceOrderInputDto, ServiceOrderWithHistoryOutputDto>
