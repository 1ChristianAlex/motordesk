package com.khrix.infrastructure.http

import com.auth0.jwt.JWT
import com.khrix.infrastructure.http.core.AppRouting
import com.khrix.infrastructure.http.core.HttpResult
import com.khrix.infrastructure.security.JwtConfig
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.di.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.slf4j.event.Level

fun Application.httpApplication() {
    httpHeaders()
    appRoute()
    logging()
}

private fun Application.appRoute() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
    install(Resources)
    bindAuth()
    bindRoutes()
}

private fun Application.bindRoutes() {
    routing {
        val appRoutingList: List<AppRouting> by dependencies
        appRoutingList.forEach {
            it.map(this)
        }
    }
}

private fun Application.bindAuth() {
    val jwtConfig: JwtConfig by dependencies
    install(Authentication) {
        jwt("auth-jwt") {
            realm = jwtConfig.realm
            verifier(
                JWT
                    .require(jwtConfig.algorithm)
                    .withAudience(jwtConfig.audience)
                    .withIssuer(jwtConfig.issuer)
                    .build()
            )
            challenge { defaultScheme, realm ->
                call.respond(HttpResult(null, HttpStatusCode.Unauthorized, null))
            }
            validate { credential ->
                val userId = credential.payload.getClaim("userId").asInt()
                if (userId != null && userId > 0) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
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
        options { call, outgoingContent ->
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
        allowHeader("MyCustomHeader")
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }
    install(Compression)
    install(DefaultHeaders) {
        header("X-Engine", "Ktor") // will send this header with each response
    }
}