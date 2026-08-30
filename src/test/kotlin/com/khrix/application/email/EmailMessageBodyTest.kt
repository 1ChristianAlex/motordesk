package com.khrix.application.email

import com.khrix.testutils.sampleEmailQueueItem
import kotlin.test.Test

class EmailMessageBodyTest {
    val instance = sampleEmailQueueItem()

    @Test
    fun `should convert EmailQueueItem to EmailMessageBody for approval template`() {
        val emailMessageBody = instance.toApprovalEmail("testapprovallink", true)
        assert(emailMessageBody.toRecipients.size == 1)
        assert(emailMessageBody.subject == instance.subject)
        assert(emailMessageBody.body.isNotEmpty())
    }

    @Test
    fun `should convert EmailQueueItem to EmailMessageBody for status update template`() {
        val emailMessageBody = instance.toStatusUpdateEmail()
        assert(emailMessageBody.toRecipients.size == 1)
        assert(emailMessageBody.subject == instance.subject)
        assert(emailMessageBody.body.isNotEmpty())
    }
}
