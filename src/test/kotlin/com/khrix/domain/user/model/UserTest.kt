package com.khrix.domain.user.model

import com.khrix.domain.valueobject.user.Password
import com.khrix.testutils.sampleUser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class UserTest {
    @Test
    fun `updates password address and company immutably`() {
        val user = sampleUser()
        assertIs<Password.Hashed>(user.updatePassword("hash").password)
        assertEquals(9, user.updateAddress(9).addressId)
        assertEquals(4, user.updateCompany(4).companyId)
        assertEquals(1, user.addressId)
    }
}

class RoleTest {
    @Test
    fun `contains all authorization roles`() = assertEquals(4, Role.entries.size)
}
