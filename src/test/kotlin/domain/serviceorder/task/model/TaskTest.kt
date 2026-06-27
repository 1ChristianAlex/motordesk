package com.khrix.domain.serviceorder.task.model

import com.khrix.domain.valueobject.Price
import com.khrix.domain.valueobject.ValidationErrorResult
import java.math.BigDecimal
import testutils.sampleTask
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TaskTest {
    @Test
    fun `changes price and estimated duration immutably`() {
        val task = sampleTask()
        assertEquals(BigDecimal("200.00"), task.changePrice(Price(BigDecimal("200.00"))).price.value)
        assertEquals(45, task.changeEstimatedMinutes(45).estimatedMinutes)
        assertEquals(30, task.estimatedMinutes)
    }

    @Test
    fun `rejects invalid duration`() {
        assertFailsWith<ValidationErrorResult> { sampleTask().copy(estimatedMinutes = 17) }
    }
}

class TaskCategoryTest {
    @Test fun `contains supported categories`() = assertEquals(TaskCategory.ENGINE, TaskCategory.valueOf("ENGINE"))
}

class TaskProgressStatusTest {
    @Test fun `contains progress lifecycle`() = assertEquals(3, TaskProgressStatus.entries.size)
}
