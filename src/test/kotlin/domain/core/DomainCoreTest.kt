package com.khrix.domain.core

import com.khrix.domain.core.mask.maskString
import com.khrix.domain.core.serializer.DecimalAsStringSerializer
import kotlinx.serialization.json.Json
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DateExtTest {
    @Test
    fun `current UTC date is populated`() {
        assertTrue(getCurrentUtcDateTime().year >= 2020)
    }
}

class MaskDomainPropertyTest {
    @Test
    fun `mask keeps edges and requested punctuation`() {
        assertEquals("123.****.*90", maskString("123.4567.890", start = 3, end = 3, extra = listOf('.')))
    }
}

class DecimalAsStringSerializerTest {
    @Test
    fun `serializes decimal without losing scale`() {
        val value = BigDecimal("12.340")
        val encoded = Json.encodeToString(DecimalAsStringSerializer, value)
        assertEquals("\"12.340\"", encoded)
        assertEquals(value, Json.decodeFromString(DecimalAsStringSerializer, encoded))
    }
}
