val libs = project.extensions.getByType<VersionCatalogsExtension>().named("libs")

plugins {
    kotlin("jvm")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType(org.gradle.api.tasks.testing.AbstractTestTask::class.java) {
    testLogging.events = setOf(
        org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
        org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
        org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
    )
}

dependencies {
    "implementation"(libs.findLibrary("kotlinx-coroutines-core").get())
    "testImplementation"(libs.findLibrary("kotlin-test").get())
}
