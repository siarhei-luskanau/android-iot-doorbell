import org.apache.tools.ant.taskdefs.condition.Os

const val ENV_EMULATOR_AVD_NAME = "ENV_EMULATOR_AVD_NAME"

data class EmulatorConfig(
    val avdName: String,
    val sdkId: String,
    val deviceType: String,
    val port: String
)

val ANDROID_EMULATORS = listOf(
    EmulatorConfig(
        avdName = "TestEmulator23",
        sdkId = "system-images;android-23;google_apis;x86_64",
        deviceType = "Nexus One",
        port = "5560"
    )
//    ,
//    EmulatorConfig(
//        avdName = "TestEmulator30",
//        sdkId = "system-images;android-30;google_apis;x86_64",
//        deviceType = "Nexus 5X",
//        port = "5566"
//    )
    ,
    EmulatorConfig(
        avdName = "TestEmulator28",
        sdkId = "system-images;android-28;google_apis;x86_64",
        deviceType = "Galaxy Nexus",
        port = "5562"
    )
    ,
    EmulatorConfig(
        avdName = "TestEmulator29",
        sdkId = "system-images;android-29;google_apis;x86_64",
        deviceType = "Nexus 5X",
        port = "5564"
    )
)

fun platformExecutable(name: String, ext: String = "exe"): String =
    if (Os.isFamily(Os.FAMILY_WINDOWS)) {
        "$name.$ext"
    } else {
        name
    }
