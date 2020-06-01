tasks.register("ciBuildApp") {
    doLast {
        gradlew(
            "setupAndroidSDK",
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

        gradlew(
            "killAndroidEmulator",
            "setupAndroidEmulator"
        )

        ANDROID_EMULATORS.forEach { emulatorConfig ->
            gradlew(
                "runAndroidEmulator",
                addToEnvironment = mapOf(ENV_EMULATOR_AVD_NAME to emulatorConfig.avdName)
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
