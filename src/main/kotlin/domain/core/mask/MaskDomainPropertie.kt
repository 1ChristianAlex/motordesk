package com.khrix.domain.core.mask

fun maskString(value: String, start: Int = 3, end: Int = 6, extra: List<Char> = listOf()): String {
    return value.mapIndexed { index, letter ->
        if (index < start || index > (value.length - end) || letter in extra) {
            letter
        } else {
            "*"
        }
    }.joinToString("")
}

fun maskString(value: String, divider: Int, extra: List<Char> = listOf()): String {
    return maskString(value, value.length / divider, value.length / (divider + 1), extra)
}