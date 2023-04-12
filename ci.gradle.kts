import java.util.Properties
import org.apache.tools.ant.taskdefs.condition.Os

val CI_GRADLE = "CI_GRADLE"

tasks.register("ciLint") {
    group = CI_GRADLE
    doLast {
        gradlew(
            "ktlintCheck",
            "detekt",
            "lintDebug"
        )
    }
}

tasks.register("ciUnitTest") {
    group = CI_GRADLE
    doLast {
        gradlew(
            "clean",
            "koverVerify",
            "koverReport",
            "koverMergedReport"
        )
    }
}

tasks.register("ciBuildApp") {
    group = CI_GRADLE
    doLast {
        gradlew(
            "assembleDebug"
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

tasks.register("ciEmulator30") {
    group = CI_GRADLE
    doLast {
        runOnEmulator("TestEmulator30")
    }
}

tasks.register("ciEmulator31") {
    group = CI_GRADLE
    doLast {
        runOnEmulator(emulatorName = "TestEmulator31")
    }
}

tasks.register("ciEmulator32") {
    group = CI_GRADLE
    doLast {
        runOnEmulator(emulatorName = "TestEmulator32")
    }
}

tasks.register("ciEmulator33") {
    group = CI_GRADLE
    doLast {
        runOnEmulator(emulatorName = "TestEmulator33")
    }
}

tasks.register("ciAll") {
    group = CI_GRADLE
    doLast {
        gradlew(
            "clean",
            "ktlintFormat",
            "ciLint",
            "ciUnitTest",
            "ciBuildApp",
            "ciEmulator30",
            "ciEmulator31",
            "ciEmulator32",
            "ciEmulator33"
        )
    }
}

tasks.register("ciSetupAndroid") {
    group = CI_GRADLE
    doLast {
        gradlew(
            "setupAndroidCmdlineTools",
            isAndroidSdkGradlew = true
        )
        gradlew(
            "setupAndroidSDK",
            isAndroidSdkGradlew = true
        )
    }
}

fun runOnEmulator(
    emulatorName: String,
    directorySuffix: String = emulatorName
) {
    gradlew(
        "setupAndroidSDK",
        "killAndroidEmulator",
        addToSystemProperties = mapOf(GradleArguments.EMULATOR_AVD_NAME to emulatorName),
        isAndroidSdkGradlew = true
    )
    gradlew(
        "setupAndroidEmulator",
        addToSystemProperties = mapOf(GradleArguments.EMULATOR_AVD_NAME to emulatorName),
        isAndroidSdkGradlew = true
    )
    gradlew("assembleDebugAndroidTest")
    Thread {
        gradlew(
            "runAndroidEmulator",
            addToSystemProperties = mapOf(GradleArguments.EMULATOR_AVD_NAME to emulatorName),
            isAndroidSdkGradlew = true
        )
    }.start()
    gradlew("waitAndroidEmulator", isAndroidSdkGradlew = true)
    gradlew(
        "executeScreenshotTests",
        "-PdirectorySuffix=$directorySuffix",
        "-Precord"
    )
}

fun gradlew(
    vararg tasks: String,
    addToSystemProperties: Map<String, String>? = null,
    isAndroidSdkGradlew: Boolean = false
) {
    exec {
        executable = File(
            project.rootDir,
            pathOf(
                if (isAndroidSdkGradlew) "android_sdk" else null,
                if (Os.isFamily(Os.FAMILY_WINDOWS)) "gradlew.bat" else "gradlew"
            )
        )
            .also { it.setExecutable(true) }
            .absolutePath
        if (isAndroidSdkGradlew) {
            workingDir = File(project.rootDir, "android_sdk")
        }
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
        }?.getProperty("sdk.dir")
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
        println("commandLine: ${this.commandLine}")
    }.apply { println("ExecResult: $this") }
}
