package com.khrix.infrastructure.sqids


import com.khrix.domain.core.shortid.ShortId
import io.ktor.server.plugins.di.*

fun sqIdsDI(dependencies: DependencyRegistry) {
    with(dependencies) {
        provide<ShortId>(SqIdsShortIdImpl::class)
    }
}