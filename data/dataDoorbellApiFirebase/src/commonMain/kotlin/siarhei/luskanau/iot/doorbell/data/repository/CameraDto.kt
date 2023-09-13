package siarhei.luskanau.iot.doorbell.data.repository

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CameraDto(
    @SerialName("camera_id") val cameraId: String,
    @SerialName("name") val name: String?,
    @SerialName("sizes") val sizes: Map<String, SizeDto>? = null,
    @SerialName("info") val info: CameraInfoDto? = null,
    @SerialName("camerax_info") val cameraxInfo: CameraxInfoDto? = null
)

@Serializable
data class CameraInfoDto(
    @SerialName("LENS_FACING")
    val lensFacing: String? = null,
    @SerialName("INFO_SUPPORTED_HARDWARE_LEVEL")
    val infoSupportedHardwareLevel: String? = null,
    @SerialName("SCALER_STREAM_CONFIGURATION_MAP")
    val scalerStreamConfigurationMap: Map<String, Map<String, String>>? = null,
    @SerialName("CONTROL_AVAILABLE_EFFECTS")
    val controlAvailableEffects: Map<String, String>? = null,
    @SerialName("error")
    val error: String? = null
)

@Serializable
data class CameraxInfoDto(
    @SerialName("error") val error: String? = null
)
