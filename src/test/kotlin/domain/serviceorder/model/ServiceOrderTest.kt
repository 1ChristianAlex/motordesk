package com.khrix.domain.serviceorder.model

import com.khrix.domain.user.model.Role
import com.khrix.domain.valueobject.ValidationErrorResult
import java.math.BigDecimal
import testutils.sampleInventoryItem
import testutils.sampleServiceOrder
import testutils.sampleTask
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class ServiceOrderTest {
    @Test
    fun `normalizes code and calculates totals and duration`() {
        val order = sampleServiceOrder(tasks = listOf(sampleTask()), inventoryItems = listOf(sampleInventoryItem())).apply { code = "abc" }
        assertEquals("#abc", order.code)
        assertEquals(30, order.expectedMinutes)
        assertEquals(BigDecimal("200.00"), order.totalPrice)
    }

    @Test
    fun `changing task ids moves order to waiting approval`() {
        val order = sampleServiceOrder()
        val updated = order.updateTasks(listOf(sampleTask(2)))
        assertEquals(ServiceOrderStatus.WAITING_APPROVAL, updated.status)
        assertSame(order, order.updateTasks(order.tasks))
    }

    @Test
    fun `validates client role and vehicle ownership`() {
        assertFailsWith<ValidationErrorResult> { sampleServiceOrder(client = testutils.sampleUser(role = Role.MANAGER)) }
        assertFailsWith<ValidationErrorResult> { sampleServiceOrder(vehicle = testutils.sampleVehicle(ownerId = 99)) }
    }

    @Test
    fun `diagnosis can only change while in diagnosis`() {
        assertFailsWith<IllegalArgumentException> { sampleServiceOrder().updateDiagnosis("diagnosed") }
        val order = sampleServiceOrder().copy(status = ServiceOrderStatus.IN_DIAGNOSIS)
        assertEquals("diagnosed", order.updateDiagnosis("diagnosed").diagnosis)
    }
}

class ServiceOrderStatusTest {
    @Test
    fun `checks status permissions by role`() {
        assertEquals(true, ServiceOrderStatus.IN_PROGRESS.checkRole(Role.ENGINEER))
        assertEquals(false, ServiceOrderStatus.CREATED.checkRole(Role.ENGINEER))
        assertEquals(true, ServiceOrderStatus.CANCELLED.checkRole(Role.MANAGER))
        assertEquals(false, ServiceOrderStatus.CREATED.checkRole(Role.CLIENT))
    }
}

class ServiceOrderTaskTest {
    @Test fun `retains task progress association`() {
        val relation = ServiceOrderTask(1, 2, com.khrix.domain.serviceorder.task.model.TaskProgressStatus.COMPLETE)
        assertEquals(2, relation.taskId)
    }
}
