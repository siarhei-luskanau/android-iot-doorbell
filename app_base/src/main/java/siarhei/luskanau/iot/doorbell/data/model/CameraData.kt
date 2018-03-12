package siarhei.luskanau.iot.doorbell.data.model

import com.google.gson.annotations.SerializedName

data class CameraData(
        @SerializedName("camera_id") val cameraId: String,
        @SerializedName("info") val info: Map<String, Any>
)
