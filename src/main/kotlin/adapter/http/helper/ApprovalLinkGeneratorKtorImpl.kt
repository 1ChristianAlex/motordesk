package com.khrix.adapter.http.helper

import com.khrix.adapter.app.InfraConfig
import com.khrix.adapter.http.controllers.serviceorder.resources.ServiceOrderResource
import com.khrix.application.serviceorder.ApprovalLinkGenerator
import io.ktor.http.URLBuilder
import io.ktor.server.application.Application
import io.ktor.server.plugins.di.annotations.Named
import io.ktor.server.resources.href

class ApprovalLinkGeneratorKtorImpl(
    @Named("ktorApplication") private val ktorApplication: Application,
    private val infraConfig: InfraConfig,
) : ApprovalLinkGenerator {
    override fun generate(
        token: String,
        code: String,
    ): String {
        with(ktorApplication) {
            val urlBuilder =
                URLBuilder(
                    host = infraConfig.serverConfig.host,
                    protocol = infraConfig.serverConfig.protocol,
                ).apply {
                    infraConfig.serverConfig.port?.let {
                        port = it
                    }
                }
            href(
                ServiceOrderResource.Client.Individual.Approval(
                    token = token,
                    parent = ServiceOrderResource.Client.Individual(code = code),
                ),
                urlBuilder,
            )

            return urlBuilder.buildString()
        }
    }
}
