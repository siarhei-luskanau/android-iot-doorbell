package siarhei.luskanau.iot.doorbell.data.repository

import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.media.Image
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import android.view.WindowManager
import io.reactivex.Completable
import io.reactivex.Observable
import siarhei.luskanau.iot.doorbell.data.repository.rx.capture.RxCaptureEvent
import siarhei.luskanau.iot.doorbell.data.repository.rx.capture.RxCaptureManager
import siarhei.luskanau.iot.doorbell.data.repository.rx.configure.RxConfigureSessionEvent
import siarhei.luskanau.iot.doorbell.data.repository.rx.configure.RxConfigureSessionManager
import siarhei.luskanau.iot.doorbell.data.repository.rx.open.RxOpenCameraEvent
import siarhei.luskanau.iot.doorbell.data.repository.rx.open.RxOpenCameraManager
import timber.log.Timber
import java.nio.ByteBuffer

class AndroidCameraRepository(
        private val context: Context
) : CameraRepository {

    companion object {
        private const val IMAGE_WIDTH = 320
        private const val IMAGE_HEIGHT = 240
        private const val MAX_IMAGES = 1
        private val ORIENTATIONS = mapOf(
                Pair(Surface.ROTATION_0, 90),
                Pair(Surface.ROTATION_90, 0),
                Pair(Surface.ROTATION_180, 270),
                Pair(Surface.ROTATION_270, 180)
        )
    }

    override fun makeImage(deviceId: String, cameraId: String): Observable<ByteArray> =
            ImageCompressor()
                    .scale(
                            openCamera(cameraId),
                            IMAGE_WIDTH
                    )
                    .doOnNext { image: ByteArray ->
                        Timber.d("makeAndSendImage image:${image.size}")
                    }
                    .onErrorResumeNext(Observable.empty())
                    .doOnComplete {
                        Timber.d("Camera $cameraId makeAndSendImage.doOnComplete")
                    }

    private fun openCamera(cameraId: String): Observable<ByteArray> {

        val backgroundThread = HandlerThread("CameraBackground:$cameraId")
        backgroundThread.start()
        val backgroundHandler = Handler(backgroundThread.looper)

        return RxOpenCameraManager()
                .openCamera(
                        context.getSystemService(Context.CAMERA_SERVICE) as CameraManager?,
                        cameraId,
                        backgroundHandler
                )
                .flatMap { rxOpenCameraEvent: RxOpenCameraEvent ->
                    Timber.d("Camera ${rxOpenCameraEvent.camera.id} openCamera: ${rxOpenCameraEvent.eventType}")
                    when (rxOpenCameraEvent.eventType) {
                        RxOpenCameraEvent.OPENED -> createCaptureSession(rxOpenCameraEvent.camera)
                        else -> Observable.empty<ByteArray>()
                    }
                }
                .doOnError {
                    Timber.e(it)
                }
                .doOnComplete {
                    Timber.d("Camera $cameraId openCamera.doOnComplete")
                }

    }

    private fun createCaptureSession(camera: CameraDevice): Observable<ByteArray> {
        // Initialize the image processor
        val imageReader = ImageReader.newInstance(IMAGE_WIDTH, IMAGE_HEIGHT, ImageFormat.JPEG, MAX_IMAGES)

        return RxConfigureSessionManager()
                .createCaptureSession(camera, listOf(imageReader.surface))
                .flatMap { rxConfigureSessionEvent: RxConfigureSessionEvent ->
                    Timber.d("Camera ${camera.id} createCaptureSession: ${rxConfigureSessionEvent.eventType}")
                    when (rxConfigureSessionEvent.eventType) {
                        RxConfigureSessionEvent.CONFIGURED -> capture(camera, rxConfigureSessionEvent.captureSession, imageReader)
                        else -> Observable.empty<ByteArray>()
                    }
                }
                .flatMap {
                    close(camera).andThen(Observable.just(it))
                }
                .doOnError {
                    Timber.e(it)
                }
                .doOnComplete {
                    Timber.d("Camera ${camera.id} createCaptureSession.doOnComplete")
                }
    }

    private fun capture(
            camera: CameraDevice,
            captureSession: CameraCaptureSession,
            imageReader: ImageReader
    ): Observable<ByteArray> {

        val captureBuilder = camera.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
        captureBuilder.addTarget(imageReader.surface)
        captureBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON)

        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager?
        windowManager?.let {
            val display = windowManager.defaultDisplay
            val rotation = display.rotation
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS[rotation])
        }

        return RxCaptureManager()
                .capture(camera, captureSession, captureBuilder.build())
                .flatMap { rxCaptureEvent: RxCaptureEvent ->
                    Timber.d("Camera ${camera.id} capture: ${rxCaptureEvent.eventType}")
                    when (rxCaptureEvent.eventType) {
                        RxCaptureEvent.CAPTURE_COMPLETED -> acquireLatestImage(camera, imageReader)
                        else -> Observable.empty<ByteArray>()
                    }
                }
                .doOnComplete {
                    Timber.d("Camera ${camera.id} capture.doOnComplete")
                }
    }

    private fun acquireLatestImage(
            camera: CameraDevice,
            imageReader: ImageReader
    ): Observable<ByteArray> = Observable
            .fromCallable {
                val image: Image? = imageReader.acquireLatestImage()
                // get image bytes
                val imageBuf: ByteBuffer? = image?.planes?.get(0)?.buffer
                val imageBytes = ByteArray(imageBuf?.remaining() ?: 0)
                imageBuf?.get(imageBytes)
                image?.close()

                imageBytes
            }
            .doOnComplete {
                Timber.d("Camera ${camera.id} acquireLatestImage.doOnComplete")
            }

    private fun close(camera: CameraDevice): Completable =
            Completable.fromAction {
                Timber.d("Camera ${camera.id} close")
                camera.close()
            }.doOnError {
                Timber.e(it)
            }

}