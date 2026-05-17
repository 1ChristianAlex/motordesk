package com.khrix.infrastructure.http.core

interface HTTPHandler<Body, Output> {
    suspend fun handler(body: Body): HTTPResult<Output>
}
