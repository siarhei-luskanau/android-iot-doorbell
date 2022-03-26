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

fun runOnEmulator(
    emulatorName: String,
    directorySuffix: String = emulatorName,
    isRecording: Boolean = System.getenv("CI").isNullOrEmpty(),
) {
    gradlew(
        "setupAndroidSDK",
        "killAndroidEmulator",
        addToEnvironment = mapOf(ENV_EMULATOR_AVD_NAME to emulatorName)
    )
    gradlew(
        "setupAndroidEmulator",
        addToEnvironment = mapOf(ENV_EMULATOR_AVD_NAME to emulatorName)
    )
    Thread {
        gradlew(
            "runAndroidEmulator",
            addToEnvironment = mapOf(ENV_EMULATOR_AVD_NAME to emulatorName)
        )
    }.start()
    gradlew("waitAndroidEmulator")
    mutableListOf(
        "executeScreenshotTests",
        "-PdirectorySuffix=$directorySuffix",
    ).also {
        if (isRecording) it.add("-Precord")
    }.also{
        gradlew(*it.toTypedArray())
    }
    gradlew("killAndroidEmulator")
}

fun gradlew(vararg tasks: String, addToEnvironment: Map<String, String>? = null) {
    exec {
        val gradlePath = File(
            project.rootDir,
            platformExecutable(name = "gradlew", ext = "bat")
        ).absolutePath
        commandLine = mutableListOf<String>().apply {
            add(gradlePath)
            addAll(tasks)
            add("--stacktrace")
        }
        addToEnvironment?.let {
            environment = environment.orEmpty().toMutableMap().apply { putAll(it) }
        }
        println("commandLine: ${this.commandLine}")
    }.apply { println("ExecResult: $this") }
}
