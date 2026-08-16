package com.khrix.application.notification.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.email.usecase.UpdateEmailQueueCommand
import com.khrix.domain.email.usecase.UpdateEmailQueueUseCase
import com.khrix.domain.serviceorder.usecase.UpdateServiceOrderCommand
import com.khrix.domain.serviceorder.usecase.UpdateServiceOrderUseCase
import com.khrix.domain.user.model.Role
import io.ktor.server.plugins.di.annotations.Named
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class UpdateEmailQueueUseCaseImpl(
    @Named("applicationScope") private val scope: CoroutineScope,
    private val updateServiceOrderUseCase: UpdateServiceOrderUseCase,
) : BaseUseCaseImpl<UpdateEmailQueueCommand, Unit>(),
    UpdateEmailQueueUseCase {
    override suspend fun internalExecute(command: UpdateEmailQueueCommand) {
        scope.launch {
            updateServiceOrderUseCase.execute(
                UpdateServiceOrderCommand(
                    code = command.code,
                    status = command.serviceOrderStatus,
                    complaint = null,
                    operatorRole = Role.ADMIN,
                ),
            )
        }
    }

    override suspend fun useCaseDescription(): String = "Create an email queue item for a service order"
}
