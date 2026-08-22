package com.khrix.adapter.inbound.http.controllers.serviceorder.resources.mappers

import com.khrix.adapter.inbound.http.controllers.serviceorder.resources.dto.ApprovesServiceOrderInputDto
import com.khrix.adapter.inbound.http.controllers.serviceorder.resources.dto.ClientServiceOrderItemInputDto
import com.khrix.adapter.inbound.http.controllers.serviceorder.resources.dto.HistoryChangesDto
import com.khrix.adapter.inbound.http.controllers.serviceorder.resources.dto.ServiceOrderInputDto
import com.khrix.adapter.inbound.http.controllers.serviceorder.resources.dto.ServiceOrderOutputDto
import com.khrix.adapter.inbound.http.controllers.serviceorder.resources.dto.ServiceOrderWithHistoryOutputDto
import com.khrix.adapter.inbound.http.controllers.serviceorder.resources.dto.UpdateServiceOrderInputDto
import com.khrix.adapter.inbound.http.controllers.user.resources.mappers.toOutputDto
import com.khrix.adapter.inbound.http.controllers.vehicles.resources.mappers.toOutputDto
import com.khrix.domain.history.model.HistoryChanges
import com.khrix.domain.serviceorder.model.ServiceOrder
import com.khrix.domain.serviceorder.usecase.ApprovesServiceOrderCommand
import com.khrix.domain.serviceorder.usecase.CreateServiceOrderCommand
import com.khrix.domain.serviceorder.usecase.GetClientServiceOrdersByCodeCommand
import com.khrix.domain.serviceorder.usecase.ServiceOrderWithHistory
import com.khrix.domain.serviceorder.usecase.UpdateServiceOrderCommand

fun ServiceOrderInputDto.toCommand(): CreateServiceOrderCommand =
    CreateServiceOrderCommand(
        clientId = this.clientId,
        operatorId = this.operatorId,
        vehicleId = this.vehicleId,
        complaint = this.complaint,
        diagnosis = this.diagnosis,
        tasksIds = this.tasksIds,
        inventoryItemsIds = this.inventoryItemsIds,
    )

fun UpdateServiceOrderInputDto.toCommand() =
    UpdateServiceOrderCommand(
        code = this.code,
        complaint = this.complaint,
        diagnosis = this.diagnosis,
        tasksIds = this.tasksIds,
        inventoryItemsIds = this.inventoryItemsIds,
        status = this.status,
        operatorRole = this.operatorRole,
    )

fun ServiceOrder.toOutputDto(): ServiceOrderOutputDto =
    ServiceOrderOutputDto(
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
        totalPrice = this.totalPrice,
        code = this.code,
    )

fun ClientServiceOrderItemInputDto.toCommand() =
    GetClientServiceOrdersByCodeCommand(
        code = this.code,
        clientId,
    )

fun ApprovesServiceOrderInputDto.toCommand() = ApprovesServiceOrderCommand(token, code)

private fun List<HistoryChanges>.toOutputDto(): List<HistoryChangesDto> =
    this.map {
        HistoryChangesDto(
            it.changedAt,
            it.changes,
        )
    }

fun ServiceOrderWithHistory.toOutputDto(): ServiceOrderWithHistoryOutputDto =
    ServiceOrderWithHistoryOutputDto(
        this.serviceOrder.toOutputDto(),
        this.changes.toOutputDto(),
    )
