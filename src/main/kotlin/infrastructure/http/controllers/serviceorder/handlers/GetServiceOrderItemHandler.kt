package com.khrix.infrastructure.http.controllers.serviceorder.handlers

import com.khrix.infrastructure.http.controllers.core.HTTPHandler
import com.khrix.infrastructure.http.controllers.serviceorder.resources.dto.ServiceOrderWithHistoryOutputDto

interface GetServiceOrderItemHandler : HTTPHandler<String, ServiceOrderWithHistoryOutputDto>
