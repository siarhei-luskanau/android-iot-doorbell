package siarhei.luskanau.iot.doorbell.data.model

data class CameraData(
    val cameraId: String,
    val name: String? = null,
    val sizes: Map<Int, SizeData>? = null,
    val info: Map<String, Any>? = null,
    val cameraxInfo: Map<String, Any>? = null
)
