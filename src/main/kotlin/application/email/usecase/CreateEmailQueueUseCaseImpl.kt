package com.khrix.application.email.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.email.model.EmailQueueItem
import com.khrix.domain.email.model.EmailStatus
import com.khrix.domain.email.model.ServiceOrderEmailMetadata
import com.khrix.domain.email.repository.EmailQueueRepository
import com.khrix.domain.email.usecase.CreateEmailQueueUseCase
import com.khrix.domain.serviceorder.model.ServiceOrder
import com.khrix.domain.user.address.repository.AddressRepository

class CreateEmailQueueUseCaseImpl(
    private val emailQueueRepository: EmailQueueRepository,
    private val addressRepository: AddressRepository,
) : CreateEmailQueueUseCase, BaseUseCaseImpl<ServiceOrder, Unit>() {
    override suspend fun internalExecute(command: ServiceOrder) {
        val clientAddress =
            addressRepository.read(command.client.addressId) ?: throw NoSuchElementException("Address is null")

        emailQueueRepository.create(
            EmailQueueItem(
                id = 0,
                recipient = command.client.email.value,
                subject = "Service Order Created",
                metadata = ServiceOrderEmailMetadata(
                    serviceOrder = command,
                    clientAddress = clientAddress
                ),
                status = EmailStatus.PENDING,
                attempts = 0,
                errorMessage = null
            )
        )
    }

    override suspend fun useCaseDescription(): String {
        return "Create an email queue item for a service order"
    }

}
