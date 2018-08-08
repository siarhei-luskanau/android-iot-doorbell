package siarhei.luskanau.iot.doorbell.data.repository.rx.open

import android.hardware.camera2.CameraDevice
import androidx.annotation.StringDef

data class RxOpenCameraEvent(
        @OpenCameraEventType val eventType: String,
        val camera: CameraDevice,
        val error: Int? = null
) {
    companion object {
        @StringDef(
                OPENED,
                DISCONNECTED,
                ERROR,
                CLOSED
        )
        @Retention(AnnotationRetention.SOURCE)
        annotation class OpenCameraEventType

        const val OPENED = "OPENED"
        const val DISCONNECTED = "DISCONNECTED"
        const val ERROR = "ERROR"
        const val CLOSED = "CLOSED"
    }
}