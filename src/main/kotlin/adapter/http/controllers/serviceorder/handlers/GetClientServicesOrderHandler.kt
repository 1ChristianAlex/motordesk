package com.khrix.adapter.http.controllers.serviceorder.handlers

import com.khrix.adapter.http.controllers.core.HTTPHandler
import com.khrix.adapter.http.controllers.serviceorder.resources.dto.ServiceOrderOutputDto

interface GetClientServicesOrderHandler : HTTPHandler<Int, List<ServiceOrderOutputDto>>
