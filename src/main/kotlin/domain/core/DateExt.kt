package com.khrix.domain.core

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

fun getCurrentUtcDateTime(): LocalDateTime {
    return Clock.System.now().toLocalDateTime(TimeZone.UTC)
}