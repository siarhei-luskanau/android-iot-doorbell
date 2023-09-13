package siarhei.luskanau.iot.doorbell.data.model

data class CameraData(
    val cameraId: String,
    val name: String? = null,
    val sizes: Map<String, SizeData>? = null,
    val info: CameraInfoData? = null,
    val cameraxInfo: CameraxInfoData? = null
)

data class CameraInfoData(
    val lensFacing: String? = null,
    val infoSupportedHardwareLevel: String? = null,
    val scalerStreamConfigurationMap: Map<String, Map<String, String>>? = null,
    val controlAvailableEffects: Map<String, String>? = null,
    val error: String? = null
)

data class CameraxInfoData(
    val implementationType: String? = null,
    val sensorRotationDegrees: String? = null,
    val hasFlashUnit: String? = null,
    val toString: String? = null,
    val error: String? = null
)
