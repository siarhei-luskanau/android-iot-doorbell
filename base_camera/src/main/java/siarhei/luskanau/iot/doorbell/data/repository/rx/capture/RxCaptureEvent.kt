package siarhei.luskanau.iot.doorbell.data.repository.rx.capture

import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.CaptureResult
import android.hardware.camera2.TotalCaptureResult
import androidx.annotation.StringDef

data class RxCaptureEvent(
        @CaptureEventType val eventType: String,
        var session: CameraCaptureSession,
        var request: CaptureRequest,
        var partialResult: CaptureResult? = null,
        var result: TotalCaptureResult? = null
) {
    companion object {
        @StringDef(
                CAPTURE_STARTED,
                CAPTURE_PROGRESSED,
                CAPTURE_COMPLETED,
                CAPTURE_FAILED,
                CAPTURE_SEQUENCE_COMPLETED,
                CAPTURE_SEQUENCE_ABORTED,
                CAPTURE_BUFFER_LOST
        )
        @Retention(AnnotationRetention.SOURCE)
        annotation class CaptureEventType

        const val CAPTURE_STARTED = "CAPTURE_STARTED"
        const val CAPTURE_PROGRESSED = "CAPTURE_PROGRESSED"
        const val CAPTURE_COMPLETED = "CAPTURE_COMPLETED"
        const val CAPTURE_FAILED = "CAPTURE_FAILED"
        const val CAPTURE_SEQUENCE_COMPLETED = "CAPTURE_SEQUENCE_COMPLETED"
        const val CAPTURE_SEQUENCE_ABORTED = "CAPTURE_SEQUENCE_ABORTED"
        const val CAPTURE_BUFFER_LOST = "CAPTURE_BUFFER_LOST"
    }
}