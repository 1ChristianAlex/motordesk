package com.khrix.adapter.app

import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.URLProtocol
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

data class MongoConfig(
    val url: String,
    val username: String,
    val password: String,
    val database: String,
    val options: String = "",
) {
    val connectionString: String
        get() {
            val query = listOfNotNull("authSource=admin", options.takeIf(String::isNotBlank)).joinToString("&")
            val credentials = "${encodeMongoCredential(username)}:${encodeMongoCredential(password)}"
            return "mongodb://$credentials@${url.removePrefix("mongodb://")}/$database?$query"
        }
}

private fun encodeMongoCredential(value: String): String =
    URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20")

data class RedisConfig(
    val host: String,
    val password: String,
    val port: String,
    val tls: Boolean = false,
) {
    val connectionString: String
        get() = "${if (tls) "rediss" else "redis"}://:$password@$host:$port"
}

data class ExposedConfig(
    val url: String,
    val driver: String,
    val user: String,
    val password: String,
)

data class AwsEmailConfig(
    val region: String,
    val fromAddress: String,
)

data class JwtConfig(
    val issuer: String,
    val audience: String,
    val realm: String,
    private val secret: String,
) {
    val algorithm: Algorithm = Algorithm.HMAC256(secret)
}

data class ServerConfig(
    val host: String,
    val port: Int?,
    val protocol: URLProtocol,
)

interface InfraConfig {
    val mongoConfig: MongoConfig
    val redisConfig: RedisConfig
    val exposedConfig: ExposedConfig
    val awsEmailConfig: AwsEmailConfig
    val jwtConfig: JwtConfig
    val serverConfig: ServerConfig
}

fun parseBooleanSetting(
    value: String?,
    name: String,
    default: Boolean = false,
): Boolean =
    when (value) {
        null -> default
        "true" -> true
        "false" -> false
        else -> throw IllegalArgumentException("$name must be either 'true' or 'false'")
    }
