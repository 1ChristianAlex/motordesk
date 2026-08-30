package com.khrix.application.core.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class ApplicationScope : CoroutineScope {
    private val job = SupervisorJob()

    override val coroutineContext =
        Dispatchers.Default + job

    fun shutdown() {
        job.cancel()
    }
}
