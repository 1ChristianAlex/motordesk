import java.io.File

data class Args(
    val environment: String,
    val action: String,
    val confirmApply: Boolean,
)

fun argValue(flag: String): String? {
    val index = args.indexOf(flag)
    if (index == -1 || index + 1 >= args.size) return null
    return args[index + 1]
}

fun parseArgs(): Args {
    val environment = argValue("--environment") ?: "dev"
    val action = argValue("--action") ?: "validate"
    val confirmApply = argValue("--confirm-apply") == "true"

    require(environment in setOf("dev", "qa", "prod")) {
        "Invalid --environment value: $environment. Use dev, qa, or prod."
    }
    require(action in setOf("fmt", "validate", "plan", "apply")) {
        "Invalid --action value: $action. Use fmt, validate, plan, or apply."
    }
    require(action != "apply" || confirmApply) {
        "Applying AWS infrastructure requires --confirm-apply true."
    }

    return Args(environment, action, confirmApply)
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

fun tf(vararg parts: String) = listOf("terraform", *parts)

val parsed = parseArgs()
val workingDir = File("infra/terraform/aws/envs/${parsed.environment}")

check(workingDir.isDirectory) { "Terraform environment directory does not exist: ${workingDir.path}" }

println("Using AWS environment=${parsed.environment} action=${parsed.action}")

if (parsed.action == "fmt") {
    runCommand(tf("fmt", "-check", "-recursive"), File("infra/terraform/aws"))
} else {
    runCommand(tf("fmt", "-check", "-recursive"), File("infra/terraform/aws"))

    if (parsed.action in setOf("validate", "plan", "apply")) {
        if (parsed.action in setOf("plan", "apply")) {
            val stateBucket =
                System.getenv("AWS_TF_BACKEND_BUCKET")
                    ?: error("AWS_TF_BACKEND_BUCKET is required for plan/apply.")
            val awsRegion =
                System.getenv("AWS_REGION")
                    ?: System.getenv("AWS_DEFAULT_REGION")
                    ?: error("AWS_REGION or AWS_DEFAULT_REGION is required for plan/apply.")

            runCommand(
                tf(
                    "init",
                    "-input=false",
                    "-reconfigure",
                    "-backend-config=bucket=$stateBucket",
                    "-backend-config=region=$awsRegion",
                ),
                workingDir,
            )
        } else {
            runCommand(tf("init", "-input=false", "-backend=false"), workingDir)
        }

        runCommand(tf("validate"), workingDir)
    }

    if (parsed.action == "plan") {
        runCommand(tf("plan", "-input=false"), workingDir)
    }

    if (parsed.action == "apply") {
        runCommand(tf("apply", "-input=false"), workingDir)
    }
}
