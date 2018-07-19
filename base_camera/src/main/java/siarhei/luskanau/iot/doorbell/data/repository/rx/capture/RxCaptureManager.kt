package siarhei.luskanau.iot.doorbell.data.repository.rx.capture

import android.hardware.camera2.*
import android.os.Handler
import android.view.Surface
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import timber.log.Timber

class RxCaptureManager {

    fun capture(
            camera: CameraDevice,
            captureSession: CameraCaptureSession,
            captureRequest: CaptureRequest,
            handler: Handler? = null
    ): Observable<RxCaptureEvent> =
            Observable.create { emitter: ObservableEmitter<RxCaptureEvent> ->
                try {
                    captureSession.capture(captureRequest,
                            object : CameraCaptureSession.CaptureCallback() {
                                override fun onCaptureStarted(session: CameraCaptureSession, request: CaptureRequest, timestamp: Long, frameNumber: Long) {
                                    Timber.d("Camera ${camera.id} capture: ${RxCaptureEvent.CAPTURE_STARTED}")
                                    emitter.onNext(RxCaptureEvent(
                                            eventType = RxCaptureEvent.CAPTURE_STARTED,
                                            session = session,
                                            request = request
                                    ))
                                }

                                override fun onCaptureProgressed(session: CameraCaptureSession, request: CaptureRequest, partialResult: CaptureResult) {
                                    Timber.d("Camera ${camera.id} capture: ${RxCaptureEvent.CAPTURE_PROGRESSED}")
                                    emitter.onNext(RxCaptureEvent(
                                            eventType = RxCaptureEvent.CAPTURE_PROGRESSED,
                                            session = session,
                                            request = request,
                                            partialResult = partialResult
                                    ))
                                }

                                override fun onCaptureCompleted(session: CameraCaptureSession, request: CaptureRequest, result: TotalCaptureResult) {
                                    Timber.d("Camera ${camera.id} capture: ${RxCaptureEvent.CAPTURE_COMPLETED}")
                                    emitter.onNext(RxCaptureEvent(
                                            eventType = RxCaptureEvent.CAPTURE_COMPLETED,
                                            session = session,
                                            request = request,
                                            result = result
                                    ))
                                    emitter.onComplete()
                                }

                                override fun onCaptureFailed(session: CameraCaptureSession, request: CaptureRequest, failure: CaptureFailure) {
                                    Timber.d("Camera ${camera.id} capture: ${RxCaptureEvent.CAPTURE_FAILED}")
                                }

                                override fun onCaptureSequenceCompleted(session: CameraCaptureSession, sequenceId: Int, frameNumber: Long) {
                                    Timber.d("Camera ${camera.id} capture: ${RxCaptureEvent.CAPTURE_SEQUENCE_COMPLETED}")
                                }

                                override fun onCaptureSequenceAborted(session: CameraCaptureSession, sequenceId: Int) {
                                    Timber.d("Camera ${camera.id} capture: ${RxCaptureEvent.CAPTURE_SEQUENCE_ABORTED}")
                                }

                                override fun onCaptureBufferLost(session: CameraCaptureSession, request: CaptureRequest, target: Surface, frameNumber: Long) {
                                    Timber.d("Camera ${camera.id} capture: ${RxCaptureEvent.CAPTURE_BUFFER_LOST}")
                                }
                            },
                            handler
                    )
                } catch (t: Throwable) {
                    emitter.onError(t)
                }
            }

}