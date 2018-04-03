package siarhei.luskanau.iot.doorbell.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import android.view.WindowManager
import io.reactivex.Completable
import io.reactivex.Observable
import timber.log.Timber

class AndroidCameraRepository(
        private val context: Context
) : CameraRepository {

    companion object {
        private val IMAGE_WIDTH = 320
        private val IMAGE_HEIGHT = 240
        private val MAX_IMAGES = 1
        private val ORIENTATIONS = mapOf(
                Pair(Surface.ROTATION_0, 90),
                Pair(Surface.ROTATION_90, 0),
                Pair(Surface.ROTATION_180, 270),
                Pair(Surface.ROTATION_270, 180)
        )
    }

    override fun makeAndSendImage(deviceId: String, cameraId: String): Completable =
            Completable.fromObservable(
                    ImageCompressor().scale(
                            createCameraObservable(cameraId).map {
                                Timber.d("image: " + it.size)
                                it
                            },
                            IMAGE_WIDTH
                    )
            )

    @SuppressLint("MissingPermission")
    fun createCameraObservable(cameraId: String): Observable<ByteArray> {
        return Observable.create { emitter ->
            Timber.d("Using camera id %s", cameraId)

            // Initialize the image processor
            val imageReader = ImageReader.newInstance(IMAGE_WIDTH, IMAGE_HEIGHT, ImageFormat.JPEG, MAX_IMAGES)

            val backgroundThread = HandlerThread("CameraBackground:$cameraId")
            backgroundThread.start()
            val backgroundHandler = Handler(backgroundThread.looper)

            val onImageAvailableListener = ImageReader.OnImageAvailableListener { reader ->
                val image = reader.acquireLatestImage()
                // get image bytes
                val imageBuf = image.getPlanes()[0].getBuffer()
                val imageBytes = ByteArray(imageBuf.remaining())
                imageBuf.get(imageBytes)
                image.close()

                emitter.onNext(imageBytes)
                emitter.onComplete()
            }
            imageReader.setOnImageAvailableListener(onImageAvailableListener, backgroundHandler)

            val stateCallback = object : CameraDevice.StateCallback() {

                override fun onOpened(cameraDevice: CameraDevice) {
                    Timber.d("Opened camera.")
                    // Here, we create a CameraCaptureSession for capturing still images.
                    try {
                        val sessionCallback = object : CameraCaptureSession.StateCallback() {

                            override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                                // When the session is ready, we start capture.
                                try {
                                    val captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
                                    captureBuilder.addTarget(imageReader.surface)

                                    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager?
                                    windowManager?.let {
                                        val display = windowManager.defaultDisplay
                                        val rotation = display.rotation
                                        captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS[rotation])
                                    }

                                    captureBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON)
                                    Timber.d("Session initialized.")

                                    val mCaptureCallback = object : CameraCaptureSession.CaptureCallback() {

                                        override fun onCaptureProgressed(session: CameraCaptureSession,
                                                                         request: CaptureRequest,
                                                                         partialResult: CaptureResult) {
                                            Timber.d("Partial result")
                                        }

                                        override fun onCaptureCompleted(session: CameraCaptureSession,
                                                                        request: CaptureRequest,
                                                                        result: TotalCaptureResult) {
                                            session.close()
                                            Timber.d("CaptureSession closed")
                                        }
                                    }

                                    cameraCaptureSession.capture(captureBuilder.build(), mCaptureCallback, null)
                                } catch (cae: CameraAccessException) {
                                    Timber.d("camera capture exception")
                                }

                            }

                            override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {
                                Timber.w("Failed to configure camera")
                            }
                        }

                        cameraDevice.createCaptureSession(
                                listOf(imageReader.surface),
                                sessionCallback, null)
                    } catch (cae: CameraAccessException) {
                        Timber.d(cae, "access exception while preparing pic")
                    }

                }

                override fun onDisconnected(cameraDevice: CameraDevice) {
                    Timber.d("Camera disconnected, closing.")
                    cameraDevice.close()
                }

                override fun onError(cameraDevice: CameraDevice, i: Int) {
                    Timber.d("Camera device error, closing.")
                    emitter.onError(RuntimeException("CameraDevice:StateCallback:onError $i"))
                    cameraDevice.close()
                }

                override fun onClosed(cameraDevice: CameraDevice) {
                    Timber.d("Closed camera, releasing")
                }
            }

            // Open the camera resource
            try {
                val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager

                cameraManager.openCamera(cameraId, stateCallback, backgroundHandler)
            } catch (cae: CameraAccessException) {
                Timber.d(cae, "Camera access exception")
                emitter.onError(cae)
            }
        }
    }

}