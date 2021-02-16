package siarhei.luskanau.iot.doorbell.data.repository

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageDto(
    @Json(name = "image_id") val imageId: String,
    @Json(name = "image_storage_path") val imageStoragePath: String?,
    @Json(name = "camera_id") val cameraId: String,
    @Json(name = "doorbell_id") val doorbellId: String,
    @Json(name = "timestamp") val timestamp: Long
)
