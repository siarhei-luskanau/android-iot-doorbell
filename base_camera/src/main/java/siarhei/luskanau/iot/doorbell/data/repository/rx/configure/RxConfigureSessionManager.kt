package siarhei.luskanau.iot.doorbell.data.repository.rx.configure

import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.os.Handler
import android.view.Surface
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import timber.log.Timber

class RxConfigureSessionManager {

    fun createCaptureSession(
            camera: CameraDevice,
            outputs: List<Surface>,
            handler: Handler? = null
    ): Observable<RxConfigureSessionEvent> =
            Observable.create({ emitter: ObservableEmitter<RxConfigureSessionEvent> ->
                try {
                    camera.createCaptureSession(
                            outputs,
                            object : CameraCaptureSession.StateCallback() {
                                override fun onConfigured(session: CameraCaptureSession) {
                                    Timber.d("Camera ${camera.id} createCaptureSession: ${RxConfigureSessionEvent.CONFIGURED}")
                                    emitter.onNext(RxConfigureSessionEvent(RxConfigureSessionEvent.CONFIGURED, session))
                                    emitter.onComplete()
                                }

                                override fun onConfigureFailed(session: CameraCaptureSession) {
                                    Timber.d("Camera ${camera.id} createCaptureSession: ${RxConfigureSessionEvent.CONFIGURE_FAILED}")
                                    emitter.onNext(RxConfigureSessionEvent(RxConfigureSessionEvent.CONFIGURE_FAILED, session))
                                    emitter.onComplete()
                                }
                            },
                            handler
                    )
                } catch (t: Throwable) {
                    emitter.onError(t)
                }
            })
}