import com.codingfeline.buildkonfig.compiler.FieldSpec.Type

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.buildkonfig)
    alias(ktorLibs.plugins.ktor)
    jacoco
    alias(libs.plugins.sonarqube)
}

group = "com.khrix"
version = "1.0.0-SNAPSHOT"

kotlin {
    jvmToolchain(21)
}

application {
    mainClass = "com.khrix.MainKt"
}

buildkonfig {
    packageName = "com.khrix"

    defaultConfigs {
        buildConfigField(Type.STRING, "PROPERTIES_FILE", "secrets.properties")
    }
}

ktor {
}

sonar {
    properties {
        property("sonar.projectKey", "Motor-Desk")
        property("sonar.host.url", "http://localhost:9000/")
        property("sonar.login", "sqp_41ef73321131622ad7143e4717d1601c0d03d5e6")
        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            "${layout.buildDirectory.get()}/reports/jacoco/test/jacocoTestReport.xml",
        )
        property("sonar.exclusions", "**/adapter/**, **/*Dto.kt, **/config/*")
    }
}

dependencies {
    // Coroutines
    implementation(libs.bundles.coroutines)

    implementation(libs.kotlinx.html)

    // Ktor
    implementation(ktorLibs.serialization.kotlinx.json)
    implementation(ktorLibs.server.auth)
    implementation(ktorLibs.server.auth.jwt)
    implementation(ktorLibs.server.callLogging)
    implementation(ktorLibs.server.cachingHeaders)
    implementation(ktorLibs.server.cio)
    implementation(ktorLibs.server.compression)
    implementation(ktorLibs.server.contentNegotiation)
    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.cors)
    implementation(ktorLibs.server.defaultHeaders)
    implementation(ktorLibs.server.di)
    implementation(ktorLibs.server.requestValidation)
    implementation(ktorLibs.server.resources)
    implementation(ktorLibs.server.statusPages)

    // Ktor docs
    implementation(ktorLibs.server.openapi)
    implementation(ktorLibs.server.routingOpenapi)
    implementation(ktorLibs.server.swagger)

    // Validation
    implementation(libs.konform)
    implementation(libs.logback.classic)

    // Security / utility
    implementation(libs.argon2)
    implementation(libs.sqids)

    // Cache
    implementation(libs.lettuce)

    // Database
    implementation(libs.mongodb)
    implementation(libs.bundles.exposed)

    // Cloud
    implementation(libs.azure.email)

    // Test
    testImplementation(kotlin("test"))
    testImplementation(ktorLibs.server.testHost)
    testImplementation(libs.mockk)
    testImplementation("io.ktor:ktor-server-test-host-jvm:3.4.0")
}

allprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask<*>>().configureEach {
        compilerOptions {
            freeCompilerArgs.addAll(
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=io.ktor.utils.io.ExperimentalKtorApi",
                "-Xannotation-default-target=param-property",
            )
        }
    }
}

tasks.register<JavaExec>("runDev") {
    group = "application"
    description = "Runs the application with development configuration."
    val runTask = tasks.named<JavaExec>("run").get()

    classpath = runTask.classpath
    mainClass.set(runTask.mainClass)
    args = runTask.args ?: emptyList()

    jvmArgs = (runTask.jvmArgs ?: emptyList()) + "-Dio.ktor.development=true"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(false)
        csv.required.set(false)
    }
}

tasks.named("sonar") {
    dependsOn(tasks.test)
    dependsOn(tasks.jacocoTestReport)
}
