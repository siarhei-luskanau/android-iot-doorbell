apply(from = "$rootDir/emulator.gradle.kts")

tasks.register<GradleBuild>("ciRunAndroidEmulator") {
    tasks = listOf(
        "setupAndroidSDK",
        "setupAndroidEmulator",
        "runAndroidEmulator",
        "waitAndroidEmulator"
    )
}

tasks.register<GradleBuild>("ciBuildApp") {
    tasks = listOf(
        "clean",
        "ktlint",
        "detekt",
        "lint",
        "build",
        "testDebugUnitTest",
        "connectedAndroidTest"
    )
}
