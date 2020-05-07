tasks.register("ciBuildApp") {
    doLast {
        exec {
            commandLine = listOf(
                File(project.rootDir, "gradlew").absolutePath,
                "setupAndroidSDK", "setupAndroidEmulator", "runAndroidEmulator"
            )
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }

        exec {
            commandLine = listOf(
                File(project.rootDir, "gradlew").absolutePath,
                "clean",
                "ktlint",
                "detekt",
                "assembleDebug",
                "testDebugUnitTest"
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
                "copyApkArtifacts", "moveScreenshotsFromDevices"
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

tasks.register<Copy>("copyApkArtifacts") {
    from(project.subprojects.map { it.buildDir })
    include("**/*.apk")
    exclude("**/apk/androidTest/**")
    includeEmptyDirs = false
    into("$buildDir/artifacts")
}
