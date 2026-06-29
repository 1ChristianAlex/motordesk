plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(ktorLibs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
    jacoco
    alias(libs.plugins.sonarqube)
}

group = "com.khrix"
version = "1.0.0-SNAPSHOT"

application {
    mainClass = "com.khrix.MainKt"
}

kotlin {
    jvmToolchain(21)
}
ktor {
}
sonar {
    properties {
        property("sonar.projectKey", "Motor-Desk")
        property("sonar.host.url", "http://localhost:9000/")
        property("sonar.login", "sqp_943eefb8f3466db57709d156f335f3776d96e8e1")
        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            "${layout.buildDirectory.get()}/reports/jacoco/test/jacocoTestReport.xml",
        )
    }
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
