package com.khrix.application.email.di

import com.khrix.application.email.usecase.CreateEmailQueueUseCaseImpl
import com.khrix.application.email.usecase.SendEmailApprovalUseCaseImpl
import com.khrix.application.email.usecase.SendEmailUpdateUseCaseImpl
import com.khrix.application.email.usecase.UpdateEmailQueueUseCaseImpl
import com.khrix.domain.email.port.usecase.CreateEmailQueueUseCase
import com.khrix.domain.email.port.usecase.SendEmailApprovalUseCase
import com.khrix.domain.email.port.usecase.SendEmailUpdateUseCase
import com.khrix.domain.email.port.usecase.UpdateEmailQueueUseCase
import io.ktor.server.plugins.di.DependencyRegistry

fun installEmailDI(dependencies: DependencyRegistry) {
    with(dependencies) {
        provide<CreateEmailQueueUseCase>(CreateEmailQueueUseCaseImpl::class)
        provide<UpdateEmailQueueUseCase>(UpdateEmailQueueUseCaseImpl::class)
        provide<SendEmailApprovalUseCase>(SendEmailApprovalUseCaseImpl::class)
        provide<SendEmailUpdateUseCase>(SendEmailUpdateUseCaseImpl::class)
    }
}
