package com.khrix.application.inventory.di

import com.khrix.application.inventory.CreateInventoryUseCaseImpl
import com.khrix.application.inventory.DecrementItemInventoryUseCaseImpl
import com.khrix.application.inventory.DeleteInventoryUseCaseImpl
import com.khrix.application.inventory.GetInventoryByIdOrSkuUseCaseImpl
import com.khrix.application.inventory.GetInventoryByListIdOrSkuUseCaseImpl
import com.khrix.application.inventory.UpdateInventoryUseCaseImpl
import com.khrix.domain.inventory.usecase.CreateInventoryUseCase
import com.khrix.domain.inventory.usecase.DecrementItemInventoryUseCase
import com.khrix.domain.inventory.usecase.DeleteInventoryUseCase
import com.khrix.domain.inventory.usecase.GetInventoryByIdOrSkuUseCase
import com.khrix.domain.inventory.usecase.GetInventoryByListIdOrSkuUseCase
import com.khrix.domain.inventory.usecase.UpdateInventoryUseCase
import io.ktor.server.plugins.di.DependencyRegistry

fun installInventoryDI(dependencies: DependencyRegistry) {
    with(dependencies) {
        provide<CreateInventoryUseCase>(CreateInventoryUseCaseImpl::class)
        provide<DecrementItemInventoryUseCase>(DecrementItemInventoryUseCaseImpl::class)
        provide<GetInventoryByListIdOrSkuUseCase>(GetInventoryByListIdOrSkuUseCaseImpl::class)
        provide<DeleteInventoryUseCase>(DeleteInventoryUseCaseImpl::class)
        provide<GetInventoryByIdOrSkuUseCase>(GetInventoryByIdOrSkuUseCaseImpl::class)
        provide<UpdateInventoryUseCase>(UpdateInventoryUseCaseImpl::class)
    }
}
