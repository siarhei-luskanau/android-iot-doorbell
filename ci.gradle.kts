tasks.register("ciBuildApp") {
    doLast {
        exec {
            commandLine = listOf(
                File(project.rootDir, "gradlew").absolutePath,
                "setupAndroidSDK",
                "clean",
                "ktlint",
                "detekt",
                "assembleDebug",
                "copyApkArtifacts",
                "testDebugUnitTest"
            )
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }

        exec {
            commandLine = listOf(
                File(project.rootDir, "gradlew").absolutePath,
                "killAndroidEmulator",
                "setupAndroidEmulator"
            )
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }

        ANDROID_EMULATORS.forEach { emulatorConfig ->
            exec {
                commandLine = listOf(
                    File(project.rootDir, "gradlew").absolutePath,
                    "runAndroidEmulator"
                )
                environment = environment.toMutableMap().apply {
                    put(ENV_EMULATOR_AVD_NAME, emulatorConfig.avdName)
                }
                println("commandLine: ${this.commandLine}")
            }.apply { println("ExecResult: $this") }

            exec {
                commandLine = listOf(
                    File(project.rootDir, "gradlew").absolutePath,
                    "assembleAndroidTest"
                )
                println("commandLine: ${this.commandLine}")
            }.apply { println("ExecResult: $this") }

            exec {
                commandLine = listOf(
                    File(project.rootDir, "gradlew").absolutePath,
                    "waitAndroidEmulator"
                )
                println("commandLine: ${this.commandLine}")
            }.apply { println("ExecResult: $this") }

            exec {
                commandLine = listOf(
                    File(project.rootDir, "gradlew").absolutePath,
                    "connectedAndroidTest"
                )
                println("commandLine: ${this.commandLine}")
            }.apply { println("ExecResult: $this") }

            exec {
                commandLine = listOf(
                    File(project.rootDir, "gradlew").absolutePath,
                    "moveScreenshotsFromDevices"
                )
                println("commandLine: ${this.commandLine}")
            }.apply { println("ExecResult: $this") }

            exec {
                commandLine = listOf(
                    File(project.rootDir, "gradlew").absolutePath,
                    "killAndroidEmulator"
                )
                println("commandLine: ${this.commandLine}")
            }.apply { println("ExecResult: $this") }
        }
    }
}

tasks.register<Copy>("copyApkArtifacts") {
    from(project.subprojects.map { it.buildDir })
    include("**/*.apk")
    exclude("**/apk/androidTest/**")
    includeEmptyDirs = false
    into("$buildDir/artifacts")
}
