package com.khrix.domain.email.publisher

interface EventConsumer {
    suspend fun start()
}