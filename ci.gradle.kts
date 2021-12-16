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
            "koverCollectReports",
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

tasks.register("ciEmulator23") {
    group = CI_GRADLE
    doLast {
        runOnEmulator("TestEmulator23")
    }
}

tasks.register("ciEmulator26") {
    group = CI_GRADLE
    doLast {
        runOnEmulator("TestEmulator26")
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

tasks.register("ciEmulator31") {
    group = CI_GRADLE
    doLast {
        runOnEmulator("TestEmulator31")
    }
}

tasks.register("ciEmulator32") {
    group = CI_GRADLE
    doLast {
        runOnEmulator("TestEmulator32")
    }
}

fun runOnEmulator(emulatorName: String) {
    gradlew(
        "setupAndroidSDK",
        "killAndroidEmulator",
        addToEnvironment = mapOf(ENV_EMULATOR_AVD_NAME to emulatorName)
    )
    gradlew(
        "setupAndroidEmulator",
        addToEnvironment = mapOf(ENV_EMULATOR_AVD_NAME to emulatorName)
    )
    Thread{
        gradlew(
            "runAndroidEmulator",
            addToEnvironment = mapOf(ENV_EMULATOR_AVD_NAME to emulatorName)
        )
    }.start()
    gradlew(":common:common_test_ui:assembleAndroidTest")
    gradlew("waitAndroidEmulator")
    gradlew("waitAndroidEmulator")
    gradlew(":common:common_test_ui:connectedAndroidTest")

    runCatching {
        gradlew(
            ":app:app_kodein:connectedAndroidTest",
            ":app:app_singleton:connectedAndroidTest",
            ":app:app_toothpick:connectedAndroidTest",
            ":app:dagger:app_dagger:connectedAndroidTest",
            ":app:koin:app_koin:connectedAndroidTest",
            ":ui:ui_doorbell_list:connectedAndroidTest",
            ":ui:ui_image_details:connectedAndroidTest",
            ":ui:ui_image_list:connectedAndroidTest",
        )
        gradlew("moveScreenshotsFromDevices")
        gradlew("killAndroidEmulator")
    }.onFailure {
        gradlew("moveScreenshotsFromDevices")
        throw it
    }
}

fun gradlew(vararg tasks: String, addToEnvironment: Map<String, String>? = null) {
    runCatching {
        exec {
            commandLine(listOf("df", "-h"))
            println(commandLine)
        }
    }
    runCatching {
        exec {
            commandLine(listOf("free", "-m"))
            println(commandLine)
        }
    }
    runCatching {
        exec {
            commandLine(
                listOf(
                    "vm_stat",
                    "|",
                    "perl",
                    "-ne",
                    "'/page size of (\\d+)/ and \$size=\$1; /Pages\\s+([^:]+)[^\\d]+(\\d+)/ and printf(\"%-16s % 16.2f Mi\\n\", \"\$1:\", \$2 * \$size / 1048576);'",
                )
            )
            println(commandLine)
        }
    }


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

    runCatching {
        exec {
            commandLine(listOf("df", "-h"))
            println(commandLine)
        }
    }
    runCatching {
        exec {
            commandLine(listOf("free", "-m"))
            println(commandLine)
        }
    }
    runCatching {
        exec {
            commandLine(
                listOf(
                    "vm_stat",
                    "|",
                    "perl",
                    "-ne",
                    "'/page size of (\\d+)/ and \$size=\$1; /Pages\\s+([^:]+)[^\\d]+(\\d+)/ and printf(\"%-16s % 16.2f Mi\\n\", \"\$1:\", \$2 * \$size / 1048576);'",
                )
            )
            println(commandLine)
        }
    }
}
