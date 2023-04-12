data class EmulatorConfig(
    val avdName: String,
    val sdkId: String,
    val deviceType: String,
    val port: String
)

val ANDROID_EMULATORS = listOf(
    EmulatorConfig(
        avdName = "TestEmulator30",
        sdkId = "system-images;android-30;aosp_atd;x86",
        deviceType = "Nexus 4",
        port = "5574"
    ),
    EmulatorConfig(
        avdName = "TestEmulator31",
        sdkId = "system-images;android-31;default;x86_64",
        deviceType = "Galaxy Nexus",
        port = "5576"
    ),
    EmulatorConfig(
        avdName = "TestEmulator32",
        sdkId = "system-images;android-32;google_apis;x86_64",
        deviceType = "Nexus 6",
        port = "5578"
    ),
    EmulatorConfig(
        avdName = "TestEmulator33",
        sdkId = "system-images;android-33;google_apis;x86_64",
        deviceType = "pixel_5",
        port = "5580"
    )
)
