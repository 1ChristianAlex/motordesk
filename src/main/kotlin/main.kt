package com.khrix

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*

fun startDockerCompose() {
    ProcessBuilder(
        "docker",
        "compose",
        "down",
        "-d"
    )
        .inheritIO()
        .start().waitFor()

    ProcessBuilder(
        "docker",
        "compose",
        "up",
        "-d"
    )
        .inheritIO()
        .start().waitFor()
}

fun main(args: Array<String>) {
    startDockerCompose()
    embeddedServer(
        factory = CIO,
        port = 8080,
        host = "0.0.0.0",
        module = Application::rootModule,
    ).start(wait = true)
}
