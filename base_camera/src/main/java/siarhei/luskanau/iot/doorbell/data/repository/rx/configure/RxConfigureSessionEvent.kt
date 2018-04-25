package siarhei.luskanau.iot.doorbell.data.repository.rx.configure

import android.hardware.camera2.CameraCaptureSession
import android.support.annotation.StringDef

data class RxConfigureSessionEvent(
        @ConfigureSessionEventType val eventType: String,
        val captureSession: CameraCaptureSession
) {
    companion object {
        @StringDef(
                CONFIGURED,
                CONFIGURE_FAILED
        )
        @Retention(AnnotationRetention.SOURCE)
        annotation class ConfigureSessionEventType

        const val CONFIGURED = "CONFIGURED"
        const val CONFIGURE_FAILED = "CONFIGURE_FAILED"
    }
}