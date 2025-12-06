import com.android.build.api.dsl.TestOptions

val EMULATOR_VERSIONS = listOf(27, 29, 31, 34, 36)

fun TestOptions.configureAndroidTestOptions() {
    unitTests {
        isIncludeAndroidResources = true
        all { test: org.gradle.api.tasks.testing.Test ->
            test.testLogging {
                exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
                events = org.gradle.api.tasks.testing.logging.TestLogEvent.entries.toSet()
            }
        }
    }
    animationsDisabled = true
    EMULATOR_VERSIONS.forEach { version ->
        managedDevices.localDevices.create("managedVirtualDevice$version") {
            device = "Pixel 2"
            apiLevel = version
        }
    }
}
