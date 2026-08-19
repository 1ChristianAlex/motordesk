package com.khrix.infrastructure.redis.event

import com.khrix.application.email.publisher.EmailEventKeys
import kotlin.test.Test
import kotlin.test.assertEquals

class RedisDataEventHandlerTest {
    @Test
    fun `wrapEvent and unwrapEvent preserve event and payload`() {
        val payload = 42

        val wrapped = RedisDataEventHandler.wrapEvent(EmailEventKeys.APPROVAL_EVENT_NAME, payload)
        val unwrapped = RedisDataEventHandler.unwrapEvent<Int>(wrapped)

        assertEquals(EmailEventKeys.APPROVAL_EVENT_NAME, unwrapped.event)
        assertEquals(payload, unwrapped.payload)
    }

    @Test
    fun `wrapEvent and unwrapEvent work with another event key`() {
        val payload = 7

        val wrapped = RedisDataEventHandler.wrapEvent(EmailEventKeys.UPDATE_EVENT_NAME, payload)
        val unwrapped = RedisDataEventHandler.unwrapEvent<Int>(wrapped)

        assertEquals(EmailEventKeys.UPDATE_EVENT_NAME, unwrapped.event)
        assertEquals(payload, unwrapped.payload)
    }
}
