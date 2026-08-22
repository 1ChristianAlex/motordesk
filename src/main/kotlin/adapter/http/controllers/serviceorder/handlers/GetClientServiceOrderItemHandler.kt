package com.khrix.adapter.http.controllers.serviceorder.handlers

import com.khrix.adapter.http.controllers.core.HTTPHandler
import com.khrix.adapter.http.controllers.serviceorder.resources.dto.ClientServiceOrderItemInputDto
import com.khrix.adapter.http.controllers.serviceorder.resources.dto.ServiceOrderWithHistoryOutputDto

interface GetClientServiceOrderItemHandler : HTTPHandler<ClientServiceOrderItemInputDto, ServiceOrderWithHistoryOutputDto>
