package com.khrix.infrastructure.http

import com.auth0.jwt.JWT
import com.khrix.domain.user.model.Role
import com.khrix.infrastructure.app.InfraCredentials
import com.khrix.infrastructure.app.JwtConfig
import com.khrix.infrastructure.http.controllers.core.AppController
import com.khrix.infrastructure.http.controllers.core.AuthNames
import com.khrix.infrastructure.http.controllers.core.HttpResult
import com.khrix.infrastructure.http.controllers.core.exceptions.HandlerException
import com.khrix.infrastructure.security.UserClaims
import io.ktor.http.CacheControl
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.content.CachingOptions
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTAuthenticationProvider
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.plugins.cachingheaders.CachingHeaders
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.defaultheaders.DefaultHeaders
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.resources.Resources
import io.ktor.server.response.respond
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import org.slf4j.event.Level

fun Application.httpApplication() {
    httpHeaders()
    appRoute()
    logging()
}

private fun Application.appRoute() {
    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            },
        )
    }
    install(StatusPages) {
        exception<HandlerException> { call, cause ->
            call.response.status(cause.statusCode)
            call.respond<HttpResult<Nothing>>(HandlerException.toHttpResultError(cause))
        }
    }
    install(Resources)
    bindAuth()
    bindRoutes()
}

private fun Application.bindRoutes() {
    routing {
        val appControllerList: List<AppController> by dependencies
        appControllerList.forEach {
            it.map(this)
        }
    }
}

private fun Application.bindAuth() {
    val infraCredentials: InfraCredentials by dependencies

    fun JWTAuthenticationProvider.Config.jwtConfigApply(allowedRoles: List<Role> = emptyList()) {
        clientJwtConfig(infraCredentials.jwtConfig)

        if (allowedRoles.isNotEmpty()) {
            validate { credential ->
                val userClaims = UserClaims.getClaims(credential.payload)
                if (userClaims.role in allowedRoles && userClaims.userId > 0) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }

    install(Authentication) {
        jwt(AuthNames.AUTHENTICATE) {
            jwtConfigApply()
        }
        jwt(AuthNames.AUTH_JWT_MANAGER) {
            jwtConfigApply(listOf(Role.MANAGER, Role.ADMIN))
        }
        jwt(AuthNames.AUTH_JWT_ENGINEER) {
            jwtConfigApply(listOf(Role.ENGINEER, Role.MANAGER, Role.ADMIN))
        }
    }
}

private fun JWTAuthenticationProvider.Config.clientJwtConfig(jwtConfig: JwtConfig) {
    realm = jwtConfig.realm
    verifier(
        JWT
            .require(jwtConfig.algorithm)
            .withAudience(jwtConfig.audience)
            .withIssuer(jwtConfig.issuer)
            .build(),
    )
    challenge { _, _ ->
        val httpResult = HandlerException.toHttpResultError<Nothing>(HandlerException.UnauthenticatedOperation())
        call.response.status(httpResult.status)
        call.respond(httpResult)
    }
    validate { credential ->
        val userClaims = UserClaims.getClaims(credential.payload)

        if (userClaims.userId > 0 && userClaims.role in Role.entries.toTypedArray()) {
            JWTPrincipal(credential.payload)
        } else {
            null
        }
    }
}

private fun Application.logging() {
    install(CallLogging) {
        level = Level.DEBUG
    }
}

private fun Application.httpHeaders() {
    install(CachingHeaders) {
        options { _, outgoingContent ->
            when (outgoingContent.contentType?.withoutParameters()) {
                ContentType.Text.CSS -> CachingOptions(CacheControl.MaxAge(maxAgeSeconds = 24 * 60 * 60))
                else -> null
            }
        }
    }
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        anyHost()
    }
    install(Compression)
    install(DefaultHeaders) {
        header("X-Engine", "Ktor")
    }
}
