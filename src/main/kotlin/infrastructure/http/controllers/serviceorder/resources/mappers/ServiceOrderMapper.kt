package com.khrix.infrastructure.http.controllers.serviceorder.resources.mappers

import com.khrix.domain.serviceorder.model.ServiceOrder
import com.khrix.domain.serviceorder.usecase.CreateServiceOrderCommand
import com.khrix.infrastructure.http.controllers.serviceorder.resources.dto.ServiceOrderInputDto
import com.khrix.infrastructure.http.controllers.serviceorder.resources.dto.ServiceOrderOutputDto
import com.khrix.infrastructure.http.controllers.user.resources.mappers.toOutputDto
import com.khrix.infrastructure.http.controllers.vehicles.resources.mappers.toOutputDto

fun ServiceOrderInputDto.toCommand(): CreateServiceOrderCommand {
    return CreateServiceOrderCommand(
        clientId = this.clientId,
        operatorId = this.operatorId,
        vehicleId = this.vehicleId,
        complaint = this.complaint,
        diagnosis = this.diagnosis,
        tasksIds = this.tasksIds,
        inventoryItemsIds = this.inventoryItemsIds
    )
}

fun ServiceOrder.toOutputDto(): ServiceOrderOutputDto {
    return ServiceOrderOutputDto(
        id = this.id,
        client = this.client.toOutputDto(true),
        operator = this.operator.toOutputDto(true),
        vehicle = this.vehicle.toOutputDto(),
        status = this.status,
        complaint = this.complaint,
        diagnosis = this.diagnosis,
        tasks = this.tasks.map { it.toOutputDto() },
        inventoryItems = this.inventoryItems.map { it.toOutputDto() },
        expectedMinutes = this.expectedMinutes,
        totalPrice = this.totalPrice
    )
}
