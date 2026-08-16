package com.khrix.application.serviceorder

interface ApprovalLinkGenerator {
    fun generate(
        token: String,
        code: String,
    ): String
}
