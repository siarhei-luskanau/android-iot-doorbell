import org.apache.tools.ant.taskdefs.condition.Os
import java.io.File
import java.io.InputStream
import java.util.Properties

val CI_GRADLE = "CI_GRADLE"

tasks.register("ciLint") {
    group = CI_GRADLE
    doLast {
        gradlew(
            "ktlintCheck",
            "detekt",
            "lintDebug",
        )
    }
}

tasks.register("ciUnitTest") {
    group = CI_GRADLE
    doLast {
        gradlew(
            "clean",
            "koverXmlReportDebug",
            "koverXmlReport",
            "koverHtmlReportDebug",
            "koverHtmlReport",
            "koverVerifyDebug",
            "koverVerify",
        )
        gradlew(
            "verifyRoborazziDebug",
            "verifyRoborazziDiDaggerDebug",
            "verifyRoborazziDiKodeinDebug",
            "verifyRoborazziDiKoinDebug",
            "verifyRoborazziDiManualDebug",
        )
    }
}

tasks.register("ciRecordScreenshots") {
    group = CI_GRADLE
    doLast {
        gradlew(
            "recordRoborazziDebug",
            "recordRoborazziDiDaggerDebug",
            "recordRoborazziDiKodeinDebug",
            "recordRoborazziDiKoinDebug",
            "recordRoborazziDiManualDebug",
        )
    }
}

tasks.register("ciBuildApp") {
    group = CI_GRADLE
    doLast {
        gradlew(
            "assembleDebug",
        )
        copy {
            from(rootProject.subprojects.map { it.buildDir })
            include("**/*.apk")
            exclude("**/apk/androidTest/**")
            eachFile { path = name }
            includeEmptyDirs = false
            into("$buildDir/apk/")
        }
    }
}

tasks.register("ciEmulatorJobsMatrixSetup") {
    group = CI_GRADLE
    doLast {
        EmulatorJobsMatrix().createMatrixJsonFile(rootProject = rootProject)
    }
}

tasks.register("ciSdkManagerLicenses") {
    group = CI_GRADLE
    doLast {
        val sdkDirPath = getAndroidSdkPath(rootDir = rootDir)
        getSdkmanagerFile(rootDir = rootDir)?.let { sdkmanagerFile ->
            val yesInputStream = object : InputStream() {
                private val yesString = "y\n"
                private var counter = 0
                override fun read(): Int = yesString[counter % 2].also { counter++ }.code
            }
            exec {
                executable = sdkmanagerFile.absolutePath
                args = listOf("--list", "--sdk_root=$sdkDirPath")
                println("exec: ${this.commandLine.joinToString(separator = " ")}")
            }.apply { println("ExecResult: $this") }
            exec {
                executable = sdkmanagerFile.absolutePath
                args = listOf("--licenses", "--sdk_root=$sdkDirPath")
                standardInput = yesInputStream
                println("exec: ${this.commandLine.joinToString(separator = " ")}")
            }.apply { println("ExecResult: $this") }
        }
    }
}

tasks.register("devAll") {
    group = CI_GRADLE
    doLast {
        gradlew("clean")
        gradlew("ktlintFormat")
        gradlew(
            "ciLint",
            "ciUnitTest",
            "ciRecordScreenshots",
            "ciBuildApp",
            "assembleAndroidTest",
        )
    }
}

tasks.register("devAllEmulator") {
    group = CI_GRADLE
    doLast {
        gradlew(
            "clean",
            "ciEmulatorJobsMatrixSetup",
        )
        gradlew("cleanManagedDevices", "--unused-only")
        EmulatorJobsMatrix().getTaskList(rootProject = rootProject).map { it.split(" ") }
            .forEach { tasks -> gradlew(*tasks.toTypedArray()) }
        gradlew("cleanManagedDevices")
    }
}

fun gradlew(
    vararg tasks: String,
    addToSystemProperties: Map<String, String>? = null,
) {
    exec {
        executable = File(
            project.rootDir,
            if (Os.isFamily(Os.FAMILY_WINDOWS)) "gradlew.bat" else "gradlew",
        )
            .also { it.setExecutable(true) }
            .absolutePath
        args = mutableListOf<String>().also { mutableArgs ->
            mutableArgs.addAll(tasks)
            addToSystemProperties?.toList()?.map { "-D${it.first}=${it.second}" }?.let {
                mutableArgs.addAll(it)
            }
            mutableArgs.add("--stacktrace")
        }
        val sdkDirPath = Properties().apply {
            val propertiesFile = File(rootDir, "local.properties")
            if (propertiesFile.exists()) {
                load(propertiesFile.inputStream())
            }
        }.getProperty("sdk.dir")
        if (sdkDirPath != null) {
            val platformToolsDir = "$sdkDirPath${java.io.File.separator}platform-tools"
            val pahtEnvironment = System.getenv("PATH").orEmpty()
            if (!pahtEnvironment.contains(platformToolsDir)) {
                environment = environment.toMutableMap().apply {
                    put("PATH", "$platformToolsDir:$pahtEnvironment")
                }
            }
        }
        if (System.getenv("JAVA_HOME") == null) {
            System.getProperty("java.home")?.let { javaHome ->
                environment = environment.toMutableMap().apply {
                    put("JAVA_HOME", javaHome)
                }
            }
        }
        if (System.getenv("ANDROID_HOME") == null) {
            environment = environment.toMutableMap().apply {
                put("ANDROID_HOME", "$sdkDirPath")
            }
        }
        println("commandLine: ${this.commandLine.joinToString(separator = " ")}")
    }.apply { println("ExecResult: $this") }
}
