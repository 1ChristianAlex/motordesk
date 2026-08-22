package com.khrix.adapter.outbound.sqids

import com.khrix.domain.core.shortid.ShortId
import io.ktor.server.plugins.di.DependencyRegistry

fun sqIdsDI(dependencies: DependencyRegistry) {
    with(dependencies) {
        provide<ShortId>(SqIdsShortIdImpl::class)
    }
}
