package com.khrix.adapter.http.controllers.serviceorder.handlers

import com.khrix.adapter.http.controllers.core.HTTPHandler
import com.khrix.adapter.http.controllers.serviceorder.resources.dto.ServiceOrderWithHistoryOutputDto
import com.khrix.adapter.http.controllers.serviceorder.resources.dto.UpdateServiceOrderInputDto

interface UpdateServiceOrderHandler : HTTPHandler<UpdateServiceOrderInputDto, ServiceOrderWithHistoryOutputDto>
