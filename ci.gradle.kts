apply(from = "$rootDir/emulator.gradle.kts")

tasks.register<GradleBuild>("ciRunAndroidEmulator") {
    tasks = listOf(
        "setupAndroidSDK",
        "setupAndroidEmulator",
        "runAndroidEmulator"
    )
}

tasks.register<GradleBuild>("ciBuildApp") {
    tasks = listOf(
        "clean",
        "ktlint",
        "detekt",
        "build",
        "testDebugUnitTest"
    )
}

tasks.register<Copy>("copyApkArtifacts") {
    from(project.subprojects.map { it.buildDir })
    include("**/*.apk")
    exclude("**/apk/androidTest/**")
    includeEmptyDirs = false
    into("$buildDir/artifacts")
}
