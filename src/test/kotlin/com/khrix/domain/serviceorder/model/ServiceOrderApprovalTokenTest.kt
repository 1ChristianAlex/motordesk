package com.khrix.domain.serviceorder.model

import com.khrix.domain.user.security.SecurityHasher
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Clock

class ServiceOrderApprovalTokenTest {
    private val hasher = mockk<SecurityHasher>()

    @Test
    fun `generateTokenHash delegates to the hasher with the salted service order code`() {
        every { hasher.hash("ABC123SALT_THIS_THING") } returns "hashed"

        val tokenHash = ServiceOrderApprovalToken.generateTokenHash(hasher, "ABC123")

        assertEquals("hashed", tokenHash)
    }

    @Test
    fun `generateExpiresAt returns a timestamp in the future`() {
        val expiresAt = ServiceOrderApprovalToken.generateExpiresAt()

        assertTrue(expiresAt > Clock.System.now())
    }

    @Test
    fun `secondary constructor populates defaults`() {
        every { hasher.hash("ABC123SALT_THIS_THING") } returns "hashed"

        val token = ServiceOrderApprovalToken("ABC123", hasher)

        assertEquals(0, token.id)
        assertEquals("ABC123", token.serviceOrderCode)
        assertEquals("hashed", token.tokenHash)
        assertEquals(null, token.usedAt)
        assertTrue(token.expiresAt > Clock.System.now())
    }
}
