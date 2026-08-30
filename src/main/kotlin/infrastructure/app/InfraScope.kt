package com.khrix.infrastructure.app

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class InfraScope : CoroutineScope {

    private val job = SupervisorJob()

    override val coroutineContext =
        Dispatchers.Default + job

    fun shutdown() {
        job.cancel()
    }
}