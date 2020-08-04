val CI_GRADLE = "CI_GRADLE"

tasks.register("ciBuildApp") {
    group = CI_GRADLE
    doLast {
        gradlew("setupAndroidSDK")
        gradlew(
            "clean",
            "ktlint",
            "detekt",
            "assembleDebug",
            "jacocoTestReportDebug",
            "jacocoTestReportMerged"
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

tasks.register("ciEmulator23") {
    group = CI_GRADLE
    doLast {
        runOnEmulator("TestEmulator23")
    }
}

tasks.register("ciEmulator28") {
    group = CI_GRADLE
    doLast {
        runOnEmulator("TestEmulator28")
    }
}

tasks.register("ciEmulator29") {
    group = CI_GRADLE
    doLast {
        runOnEmulator("TestEmulator29")
    }
}

tasks.register("ciEmulator30") {
    group = CI_GRADLE
    doLast {
        runOnEmulator("TestEmulator30")
    }
}

fun runOnEmulator(emulatorName: String) {
    gradlew(
        "setupAndroidSDK",
        addToEnvironment = mapOf(ENV_EMULATOR_AVD_NAME to emulatorName)
    )
    gradlew("killAndroidEmulator")
    gradlew(
        "setupAndroidEmulator",
        "runAndroidEmulator",
        addToEnvironment = mapOf(ENV_EMULATOR_AVD_NAME to emulatorName)
    )
    gradlew("assembleAndroidTest")
    gradlew("waitAndroidEmulator")

    runCatching {
        gradlew("connectedAndroidTest")
        gradlew("moveScreenshotsFromDevices")
        gradlew("killAndroidEmulator")
    }.onFailure {
        gradlew("moveScreenshotsFromDevices")
        throw it
    }
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
