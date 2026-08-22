package com.khrix.application.serviceorder.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.history.model.HistoryChanges
import com.khrix.domain.serviceorder.port.repository.ServiceOrderHistoryRepository
import com.khrix.domain.serviceorder.port.usecase.GetServiceOrderHistoryUseCase

class GetServiceOrderHistoryUseCaseImpl(
    private val serviceOrderHistoryRepository: ServiceOrderHistoryRepository,
) : BaseUseCaseImpl<Int, List<HistoryChanges>>(),
    GetServiceOrderHistoryUseCase {
    override suspend fun internalExecute(command: Int): List<HistoryChanges> = serviceOrderHistoryRepository.read(command) ?: listOf()

    override suspend fun useCaseDescription(): String = "Retrieve the service order history item for a given service order Id"
}
