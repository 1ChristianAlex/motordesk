package com.khrix.application.core.coroutine

import kotlinx.coroutines.Job
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ApplicationScopeTest {
    @Test
    fun `shutdown cancels application job`() {
        val scope = ApplicationScope()
        val job = scope.coroutineContext[Job]!!
        assertTrue(job.isActive)
        scope.shutdown()
        assertFalse(job.isActive)
    }
}
