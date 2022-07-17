import org.apache.tools.ant.taskdefs.condition.Os
import java.util.Properties

val CI_GRADLE = "CI_GRADLE"

tasks.register("ciLint") {
    group = CI_GRADLE
    doLast {
        gradlew(
            "clean",
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
            "koverVerify",
            "koverReport",
            "koverMergedReport",
        )
    }
}

tasks.register("ciBuildApp") {
    group = CI_GRADLE
    doLast {
        gradlew(
            "clean",
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

tasks.register("ciEmulator26") {
    group = CI_GRADLE
    doLast {
        runOnEmulator(emulatorName = "TestEmulator26")
    }
}

tasks.register("ciEmulator28") {
    group = CI_GRADLE
    doLast {
        runOnEmulator(emulatorName = "TestEmulator28")
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
            "ktlintFormat",
            "ciLint",
            "ciBuildApp",
            "ciUnitTest",
            "ciEmulator26",
            "ciEmulator28",
            "ciEmulator30",
            "ciEmulator31",
            "ciEmulator32",
            "ciEmulator33",
        )
    }
}

fun runOnEmulator(
    emulatorName: String,
    directorySuffix: String = emulatorName,
    isRecording: Boolean = System.getenv("CI").isNullOrEmpty(),
) {
    gradlew(
        "setupAndroidSDK",
        "killAndroidEmulator",
        addToEnvironment = mapOf(GradleArguments.EMULATOR_AVD_NAME to emulatorName),
        isAndroidSdkGradlew = true
    )
    gradlew(
        "setupAndroidEmulator",
        addToEnvironment = mapOf(GradleArguments.EMULATOR_AVD_NAME to emulatorName),
        isAndroidSdkGradlew = true
    )
    Thread {
        gradlew(
            "runAndroidEmulator",
            addToEnvironment = mapOf(GradleArguments.EMULATOR_AVD_NAME to emulatorName),
            isAndroidSdkGradlew = true
        )
    }.start()
    gradlew("waitAndroidEmulator", isAndroidSdkGradlew = true)
//    mutableListOf(
//        "executeScreenshotTests",
//        "-PdirectorySuffix=$directorySuffix",
//    ).also {
//        if (isRecording) it.add("-Precord")
//    }.also {
//        gradlew(*it.toTypedArray())
//    }
    gradlew("connectedAndroidTest")
    gradlew("killAndroidEmulator", isAndroidSdkGradlew = true)
    gradlew("deleteAndroidEmulator", isAndroidSdkGradlew = true)
}

fun gradlew(
    vararg tasks: String,
    addToEnvironment: Map<String, String>? = null,
    isAndroidSdkGradlew: Boolean = false,
) {
    exec {
        executable = File(
            project.rootDir,
            pathOf(
                if (isAndroidSdkGradlew) "android_sdk" else null,
                if (Os.isFamily(Os.FAMILY_WINDOWS)) "gradlew.bat" else "gradlew",
            )
        )
            .also { it.setExecutable(true) }
            .absolutePath
        if (isAndroidSdkGradlew) {
            workingDir = File(project.rootDir, "android_sdk")
        }
        args = mutableListOf<String>().apply {
            addAll(tasks)
            add("--stacktrace")
        }
        addToEnvironment?.let {
            environment = environment.toMutableMap().apply { putAll(it) }
        }
        val sdkDirPath: String = Properties().apply {
            val propertiesFile = File(rootDir, "local.properties")
            if (propertiesFile.exists()) {
                load(propertiesFile.inputStream())
            }
        }?.getProperty("sdk.dir")
        val platformToolsDir = "$sdkDirPath${java.io.File.separator}platform-tools"
        val pahtEnvironment = System.getenv("PATH").orEmpty()
        if (!pahtEnvironment.contains(platformToolsDir)) {
            environment = environment.toMutableMap().apply {
                put("PATH", "$platformToolsDir:$pahtEnvironment")
            }
        }
        println("commandLine: ${this.commandLine}")
    }.apply { println("ExecResult: $this") }
}
