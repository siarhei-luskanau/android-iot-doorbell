package siarhei.luskanau.iot.doorbell.data.repository

import android.util.Size
import com.google.gson.annotations.SerializedName

data class CameraDto(
    @SerializedName("camera_id") val cameraId: String,
    @SerializedName("name") val name: String?,
    @SerializedName("sizes") val sizes: Map<Int, Size>?,
    @SerializedName("info") val info: Map<String, Any>?,
    @SerializedName("camerax_info") val cameraxInfo: Map<String, Any>?
)
