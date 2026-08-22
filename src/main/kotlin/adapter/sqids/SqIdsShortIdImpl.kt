package com.khrix.adapter.sqids

import com.khrix.domain.core.shortid.ShortId
import org.sqids.Sqids

class SqIdsShortIdImpl : ShortId {
    override fun encode(values: List<Number>): String {
        val longList = values.map { it.toLong() }
        return Sqids().run {
            encode(longList)
        }
    }

    override fun decode(value: String): List<Long> =
        Sqids().run {
            decode(value)
        }
}
