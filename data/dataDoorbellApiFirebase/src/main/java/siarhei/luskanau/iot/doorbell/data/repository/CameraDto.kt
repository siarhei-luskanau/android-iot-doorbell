package siarhei.luskanau.iot.doorbell.data.repository

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CameraDto(
    @Json(name = "camera_id") val cameraId: String,
    @Json(name = "name") val name: String?,
    @Json(name = "sizes") val sizes: Map<Int, SizeDto>?,
    @Json(name = "info") val info: Map<String, Any>?,
    @Json(name = "camerax_info") val cameraxInfo: Map<String, Any>?
)
