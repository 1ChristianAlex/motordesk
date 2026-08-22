package com.khrix.application.serviceorder.di

import com.khrix.application.serviceorder.ServiceOrderDiffResolverImpl
import com.khrix.application.serviceorder.task.TaskDiffResolverImpl
import com.khrix.application.serviceorder.task.usecase.CreateTaskUseCaseImpl
import com.khrix.application.serviceorder.task.usecase.DeleteTaskUseCaseImpl
import com.khrix.application.serviceorder.task.usecase.GetTaskByIdUseCaseImpl
import com.khrix.application.serviceorder.task.usecase.GetTaskByListIdUseCaseImpl
import com.khrix.application.serviceorder.task.usecase.UpdateTaskUseCaseImpl
import com.khrix.application.serviceorder.usecase.ApprovesServiceOrderUseCaseImpl
import com.khrix.application.serviceorder.usecase.CreateServiceOrderHistoryUseCaseImpl
import com.khrix.application.serviceorder.usecase.CreateServiceOrderUseCaseImpl
import com.khrix.application.serviceorder.usecase.DeleteServiceOrderUseCaseImpl
import com.khrix.application.serviceorder.usecase.GetClientServiceOrdersByCodeUseCaseImpl
import com.khrix.application.serviceorder.usecase.GetServiceOrderHistoryUseCaseImpl
import com.khrix.application.serviceorder.usecase.GetServiceOrdersByClientIdUseCaseImpl
import com.khrix.application.serviceorder.usecase.GetServiceOrdersByCodeUseCaseImpl
import com.khrix.application.serviceorder.usecase.UpdateServiceOrderTaskUseCaseImpl
import com.khrix.application.serviceorder.usecase.UpdateServiceOrderUseCaseImpl
import com.khrix.domain.serviceorder.port.ServiceOrderDiffResolver
import com.khrix.domain.serviceorder.port.usecase.ApprovesServiceOrderUseCase
import com.khrix.domain.serviceorder.port.usecase.CreateServiceOrderHistoryUseCase
import com.khrix.domain.serviceorder.port.usecase.CreateServiceOrderUseCase
import com.khrix.domain.serviceorder.port.usecase.DeleteServiceOrderUseCase
import com.khrix.domain.serviceorder.port.usecase.GetClientServiceOrdersByCodeUseCase
import com.khrix.domain.serviceorder.port.usecase.GetServiceOrderHistoryUseCase
import com.khrix.domain.serviceorder.port.usecase.GetServiceOrdersByClientIdUseCase
import com.khrix.domain.serviceorder.port.usecase.GetServiceOrdersByCodeUseCase
import com.khrix.domain.serviceorder.port.usecase.UpdateServiceOrderTaskUseCase
import com.khrix.domain.serviceorder.port.usecase.UpdateServiceOrderUseCase
import com.khrix.domain.serviceorder.task.port.TaskDiffResolver
import com.khrix.domain.serviceorder.task.port.usecase.CreateTaskUseCase
import com.khrix.domain.serviceorder.task.port.usecase.DeleteTaskUseCase
import com.khrix.domain.serviceorder.task.port.usecase.GetTaskByIdUseCase
import com.khrix.domain.serviceorder.task.port.usecase.GetTaskByListIdUseCase
import com.khrix.domain.serviceorder.task.port.usecase.UpdateTaskUseCase
import io.ktor.server.plugins.di.DependencyRegistry

fun installServiceOrderDI(dependencies: DependencyRegistry) {
    with(dependencies) {
        provide<CreateTaskUseCase>(CreateTaskUseCaseImpl::class)
        provide<DeleteTaskUseCase>(DeleteTaskUseCaseImpl::class)
        provide<GetTaskByIdUseCase>(GetTaskByIdUseCaseImpl::class)
        provide<UpdateTaskUseCase>(UpdateTaskUseCaseImpl::class)
        provide<GetTaskByListIdUseCase>(GetTaskByListIdUseCaseImpl::class)
        provide<CreateServiceOrderUseCase>(CreateServiceOrderUseCaseImpl::class)
        provide<DeleteServiceOrderUseCase>(DeleteServiceOrderUseCaseImpl::class)
        provide<GetServiceOrdersByClientIdUseCase>(GetServiceOrdersByClientIdUseCaseImpl::class)
        provide<UpdateServiceOrderUseCase>(UpdateServiceOrderUseCaseImpl::class)
        provide<UpdateServiceOrderTaskUseCase>(UpdateServiceOrderTaskUseCaseImpl::class)
        provide<GetServiceOrdersByCodeUseCase>(GetServiceOrdersByCodeUseCaseImpl::class)
        provide<GetClientServiceOrdersByCodeUseCase>(GetClientServiceOrdersByCodeUseCaseImpl::class)
        provide<CreateServiceOrderHistoryUseCase>(CreateServiceOrderHistoryUseCaseImpl::class)
        provide<ApprovesServiceOrderUseCase>(ApprovesServiceOrderUseCaseImpl::class)
        provide<GetServiceOrderHistoryUseCase>(GetServiceOrderHistoryUseCaseImpl::class)
        provide<TaskDiffResolver>(TaskDiffResolverImpl::class)
        provide<ServiceOrderDiffResolver>(ServiceOrderDiffResolverImpl::class)
    }
}
