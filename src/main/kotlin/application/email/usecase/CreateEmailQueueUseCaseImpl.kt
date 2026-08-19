package com.khrix.application.email.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.email.model.EmailQueueItem
import com.khrix.domain.email.model.EmailStatus
import com.khrix.domain.email.model.ServiceOrderEmailMetadata
import com.khrix.domain.email.publisher.EmailEventKeys
import com.khrix.domain.email.publisher.EventPublisher
import com.khrix.domain.email.repository.EmailQueueRepository
import com.khrix.domain.email.usecase.CreateEmailQueueUseCase
import com.khrix.domain.serviceorder.model.ServiceOrder
import com.khrix.domain.serviceorder.model.ServiceOrderStatus
import com.khrix.domain.user.address.repository.AddressRepository
import io.ktor.server.plugins.di.annotations.Named
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class CreateEmailQueueUseCaseImpl(
    private val emailQueueRepository: EmailQueueRepository,
    private val addressRepository: AddressRepository,
    @Named("applicationScope") private val scope: CoroutineScope,
    private val eventPublisher: EventPublisher,
) : BaseUseCaseImpl<ServiceOrder, Unit>(),
    CreateEmailQueueUseCase {
    override suspend fun internalExecute(command: ServiceOrder) {
        scope.launch {
            val clientAddress =
                addressRepository.read(command.client.addressId) ?: throw NoSuchElementException("Address is null")

            val eventName =
                if (command.status ==
                    ServiceOrderStatus.CREATED
                ) {
                    EmailEventKeys.APPROVAL_EVENT_NAME
                } else {
                    EmailEventKeys.UPDATE_EVENT_NAME
                }

            val result =
                emailQueueRepository.create(
                    EmailQueueItem(
                        id = 0,
                        recipient = command.client.email.value,
                        subject =
                            if (command.status ==
                                ServiceOrderStatus.CREATED
                            ) {
                                "Sua ordem de serviço foi criada!"
                            } else {
                                "Sua ordem de serviço tem uma atualização!"
                            },
                        metadata =
                            ServiceOrderEmailMetadata(
                                serviceOrder = command,
                                clientAddress = clientAddress,
                            ),
                        status = EmailStatus.PENDING,
                        attempts = 0,
                        errorMessage = null,
                        orderCode = command.code,
                    ),
                )

            eventPublisher.publish(eventName, result)
        }
    }

    override suspend fun useCaseDescription(): String = "Create an email queue item for a service order"
}
