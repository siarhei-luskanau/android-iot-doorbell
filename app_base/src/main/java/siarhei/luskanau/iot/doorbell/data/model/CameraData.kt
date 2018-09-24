package siarhei.luskanau.iot.doorbell.data.model

import android.util.Size

data class CameraData(
        val cameraId: String,
        val name: String? = null,
        val sizes: Map<Int, Size> = emptyMap(),
        val info: Map<String, Any> = emptyMap()
)
