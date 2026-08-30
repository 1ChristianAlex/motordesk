package com.khrix.application.email.publisher

interface EventConsumer {
    suspend fun start()
}
