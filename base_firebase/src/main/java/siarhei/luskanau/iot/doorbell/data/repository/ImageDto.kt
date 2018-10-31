package siarhei.luskanau.iot.doorbell.data.repository

import com.google.gson.annotations.SerializedName

data class ImageDto(
    @SerializedName("image_id") val imageId: String,
    @SerializedName("image_storage_path") val imageStoragePath: String?,
    @SerializedName("camera_id") val cameraId: String,
    @SerializedName("device_id") val deviceId: String,
    @SerializedName("timestamp") val timestamp: Long
)
