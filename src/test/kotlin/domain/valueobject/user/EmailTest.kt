package domain.valueobject.user

import com.khrix.domain.valueobject.user.Email
import kotlin.test.Test
import kotlin.test.assertEquals

class EmailTest {
    val instance = Email("john@doe.com")

    @Test
    fun `given Email instance should mask when mask method is called`() {
        assertEquals("joh*@***.**m", instance.mask())
    }
}