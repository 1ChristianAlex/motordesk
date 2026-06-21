package com.khrix.domain.core.shortid

interface ShortId {
    fun encode(values: List<Number>): String
    fun decode(value: String): List<Long>
}