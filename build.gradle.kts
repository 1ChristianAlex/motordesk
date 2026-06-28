plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(ktorLibs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
}

group = "com.khrix"
version = "1.0.0-SNAPSHOT"

application {
    mainClass = "com.khrix.MainKt"
}

kotlin {
    jvmToolchain(25)
}
ktor {
    development = System.getenv("IS_DEV_MODE").toBoolean() ?: true
}
allprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask<*>>().configureEach {
        compilerOptions {
            freeCompilerArgs.addAll(
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=io.ktor.utils.io.ExperimentalKtorApi",
                "-Xannotation-default-target=param-property"
            )
        }
    }
}
dependencies {
    implementation(ktorLibs.serialization.kotlinx.json)
    implementation(ktorLibs.server.auth)
    implementation(ktorLibs.server.auth.jwt)
    implementation(ktorLibs.server.cachingHeaders)
    implementation(ktorLibs.server.callLogging)
    implementation(ktorLibs.server.cio)
    implementation(ktorLibs.server.compression)
    implementation(ktorLibs.server.contentNegotiation)
    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.statusPages)
    implementation(ktorLibs.server.cors)
    implementation(ktorLibs.server.defaultHeaders)
    implementation(ktorLibs.server.di)
    implementation(ktorLibs.server.requestValidation)
    implementation(ktorLibs.server.resources)

    implementation(ktorLibs.server.swagger)
    implementation(ktorLibs.server.openapi)
    implementation(ktorLibs.server.routingOpenapi)


    implementation(libs.logback.classic)
    implementation(libs.konform)

    implementation(libs.argon2)
    implementation(libs.sqids)


    implementation(libs.mongodb)

    implementation(libs.lettuce)

    implementation(libs.bundles.exposed)

    testImplementation(kotlin("test"))
    testImplementation(ktorLibs.server.testHost)
    testImplementation(libs.mockk)
    testImplementation("io.ktor:ktor-server-test-host-jvm:3.4.0")
}
