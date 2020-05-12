tasks.register("ciBuildApp") {
    doLast {
        exec {
            commandLine = listOf(
                gradlewPath(),
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
                gradlewPath(),
                "killAndroidEmulator",
                "setupAndroidEmulator"
            )
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }

        ANDROID_EMULATORS.forEach { emulatorConfig ->
            exec {
                commandLine = listOf(
                    gradlewPath(),
                    "runAndroidEmulator"
                )
                environment = environment.toMutableMap().apply {
                    put(ENV_EMULATOR_AVD_NAME, emulatorConfig.avdName)
                }
                println("commandLine: ${this.commandLine}")
            }.apply { println("ExecResult: $this") }

            exec {
                commandLine = listOf(
                    gradlewPath(),
                    "assembleAndroidTest"
                )
                println("commandLine: ${this.commandLine}")
            }.apply { println("ExecResult: $this") }

            exec {
                commandLine = listOf(
                    gradlewPath(),
                    "waitAndroidEmulator"
                )
                println("commandLine: ${this.commandLine}")
            }.apply { println("ExecResult: $this") }

            runCatching {
                exec {
                    commandLine = listOf(
                        gradlewPath(),
                        "connectedAndroidTest"
                    )
                    println("commandLine: ${this.commandLine}")
                }.apply { println("ExecResult: $this") }

                exec {
                    commandLine = listOf(
                        gradlewPath(),
                        "moveScreenshotsFromDevices"
                    )
                    println("commandLine: ${this.commandLine}")
                }.apply { println("ExecResult: $this") }

                exec {
                    commandLine = listOf(
                        gradlewPath(),
                        "killAndroidEmulator"
                    )
                    println("commandLine: ${this.commandLine}")
                }.apply { println("ExecResult: $this") }
            }.onFailure {
                exec {
                    commandLine = listOf(
                        gradlewPath(),
                        "moveScreenshotsFromDevices"
                    )
                    println("commandLine: ${this.commandLine}")
                }.apply { println("ExecResult: $this") }
                throw it
            }
        }
    }
}

fun gradlewPath() =
    File(project.rootDir, platformExecutable(name = "gradlew", ext = "bat")).absolutePath

tasks.register<Copy>("copyApkArtifacts") {
    from(project.subprojects.map { it.buildDir })
    include("**/*.apk")
    exclude("**/apk/androidTest/**")
    includeEmptyDirs = false
    into("$buildDir/artifacts")
}
