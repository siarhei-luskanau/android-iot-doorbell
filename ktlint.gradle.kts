val ktlint by configurations.creating

dependencies {
    ktlint("com.pinterest:ktlint:${PublicVersions.ktlint}")
}

val outputDir = "${project.buildDir}/reports/ktlint/"
val inputFiles = project.fileTree(mapOf("dir" to "src", "include" to "**/*.kt"))

val ktlintCheck by tasks.creating(JavaExec::class) {
    inputs.files(inputFiles)
    outputs.dir(outputDir)

    description = "Check Kotlin code style."
    classpath = ktlint
    main = "com.pinterest.ktlint.Main"
    args = listOf("--android", "src/**/*.kt")
}

val ktlintFormat by tasks.creating(JavaExec::class) {
    inputs.files(inputFiles)
    outputs.dir(outputDir)

    description = "Fix Kotlin code style deviations."
    classpath = ktlint
    main = "com.pinterest.ktlint.Main"
    args = listOf("--android", "-F", "src/**/*.kt")
}

val applyToIDEAProject by tasks.creating(JavaExec::class) {
    inputs.files(inputFiles)
    outputs.dir(outputDir)

    description = "Change the code style config files to be compliant with Android Kotlin Style Guide."
    classpath = ktlint
    main = "com.pinterest.ktlint.Main"
    args = listOf("applyToIDEAProject", "-y")
}
