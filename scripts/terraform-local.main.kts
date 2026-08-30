import java.io.ByteArrayOutputStream
import java.io.File

data class Args(
    val environment: String,
    val imageTag: String?,
    val action: String,
)

val terraformRoot = File("infra/terraform")
val backendDir = File(terraformRoot, "backend")
val azureDir = File(terraformRoot, "azure")

fun argValue(flag: String): String? {
    val index = args.indexOf(flag)
    if (index == -1 || index + 1 >= args.size) return null
    return args[index + 1]
}

fun parseArgs(): Args {
    val environment = argValue("--environment") ?: "dev"
    val imageTag = argValue("--image-tag") ?: ""
    val action = argValue("--action") ?: "plan"

    require(action == "plan" || action == "apply") {
        "Invalid --action value: $action. Use plan or apply."
    }

    return Args(environment = environment, imageTag = imageTag, action = action)
}

fun runCommand(
    command: List<String>,
    workingDir: File,
) {
    val process =
        ProcessBuilder(command)
            .directory(workingDir)
            .inheritIO()
            .start()

    val exitCode = process.waitFor()
    check(exitCode == 0) { "${command.joinToString(" ")} failed with exit code $exitCode" }
}

fun captureCommand(
    command: List<String>,
    workingDir: File,
): String {
    val output = ByteArrayOutputStream()
    val process =
        ProcessBuilder(command)
            .directory(workingDir)
            .redirectError(ProcessBuilder.Redirect.INHERIT)
            .start()

    process.inputStream.use { input -> input.copyTo(output) }
    val exitCode = process.waitFor()
    check(exitCode == 0) { "${command.joinToString(" ")} failed with exit code $exitCode" }
    return output.toString().trim()
}

fun tf(vararg parts: String) = listOf("terraform", *parts)

val parsed = parseArgs()

println("Using environment=${parsed.environment} imageTag=${parsed.imageTag} action=${parsed.action}")

runCommand(tf("init", "-input=false"), backendDir)
runCommand(tf("apply", "-input=false", "-auto-approve", "-var=environment=${parsed.environment}"), backendDir)

val resourceGroupName = captureCommand(tf("output", "-raw", "resource_group_name"), backendDir)
val storageAccountName = captureCommand(tf("output", "-raw", "storage_account_name"), backendDir)
val containerName = captureCommand(tf("output", "-raw", "container_name"), backendDir)
val stateKey = "motordesk-${parsed.environment}.tfstate"

runCommand(
    tf(
        "init",
        "-input=false",
        "-reconfigure",
        "-backend-config=resource_group_name=$resourceGroupName",
        "-backend-config=storage_account_name=$storageAccountName",
        "-backend-config=container_name=$containerName",
        "-backend-config=key=$stateKey",
    ),
    azureDir,
)

runCommand(tf("fmt", "-check", "-recursive"), azureDir)
runCommand(tf("validate"), azureDir)

when (parsed.action) {
    "plan" -> {
        runCommand(
            tf(
                "plan",
                "-input=false",
                "-var=environment=${parsed.environment}",
                "-var=image_tag=${parsed.imageTag}",
            ),
            azureDir,
        )
    }

    "apply" -> {
        runCommand(
            tf(
                "apply",
                "-input=false",
                "-auto-approve",
                "-var=environment=${parsed.environment}",
                "-var=image_tag=${parsed.imageTag}",
            ),
            azureDir,
        )
    }
}
