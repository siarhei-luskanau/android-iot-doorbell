package siarhei.luskanau.iot.doorbell.data.repository.rx.open

import android.annotation.SuppressLint
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.os.Handler
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import timber.log.Timber

class RxOpenCameraManager {

    @SuppressLint("MissingPermission")
    fun openCamera(
            cameraManager: CameraManager?,
            cameraId: String,
            handler: Handler
    ): Observable<RxOpenCameraEvent> =
            Observable.create { emitter: ObservableEmitter<RxOpenCameraEvent> ->
                try {
                    cameraManager?.openCamera(
                            cameraId,
                            object : CameraDevice.StateCallback() {
                                override fun onOpened(camera: CameraDevice) {
                                    Timber.d("Camera ${camera.id} openCamera: ${RxOpenCameraEvent.OPENED}")
                                    emitter.onNext(RxOpenCameraEvent(RxOpenCameraEvent.OPENED, camera))
                                }

                                override fun onDisconnected(camera: CameraDevice) {
                                    Timber.d("Camera ${camera.id} openCamera: ${RxOpenCameraEvent.DISCONNECTED}")
                                    emitter.onNext(RxOpenCameraEvent(RxOpenCameraEvent.DISCONNECTED, camera))
                                }

                                override fun onClosed(camera: CameraDevice) {
                                    Timber.d("Camera ${camera.id} openCamera: ${RxOpenCameraEvent.CLOSED}")
                                    emitter.onNext(RxOpenCameraEvent(RxOpenCameraEvent.CLOSED, camera))
                                    emitter.onComplete()
                                }

                                override fun onError(camera: CameraDevice, error: Int) {
                                    Timber.d("Camera ${camera.id} openCamera: ${RxOpenCameraEvent.ERROR}: $error")
                                    emitter.onNext(RxOpenCameraEvent(RxOpenCameraEvent.ERROR, camera, error))
                                }
                            },
                            handler
                    ) ?: let {
                        emitter.onComplete()
                    }
                } catch (t: Throwable) {
                    emitter.onError(t)
                }
            }

}