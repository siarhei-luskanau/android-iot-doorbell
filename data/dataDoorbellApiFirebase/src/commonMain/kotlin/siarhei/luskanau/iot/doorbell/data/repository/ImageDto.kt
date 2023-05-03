package siarhei.luskanau.iot.doorbell.data.repository

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageDto(
    @SerialName("image_id") val imageId: String,
    @SerialName("image_storage_path") val imageStoragePath: String?,
    @SerialName("camera_id") val cameraId: String,
    @SerialName("doorbell_id") val doorbellId: String,
    @SerialName("timestamp") val timestamp: Long,
)
