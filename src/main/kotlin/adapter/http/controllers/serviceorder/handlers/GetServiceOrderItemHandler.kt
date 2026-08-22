package com.khrix.adapter.http.controllers.serviceorder.handlers

import com.khrix.adapter.http.controllers.core.HTTPHandler
import com.khrix.adapter.http.controllers.serviceorder.resources.dto.ServiceOrderWithHistoryOutputDto

interface GetServiceOrderItemHandler : HTTPHandler<String, ServiceOrderWithHistoryOutputDto>
