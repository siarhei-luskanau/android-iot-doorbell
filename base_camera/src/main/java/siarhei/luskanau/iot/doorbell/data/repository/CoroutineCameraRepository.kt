package siarhei.luskanau.iot.doorbell.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureFailure
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.CaptureResult
import android.hardware.camera2.TotalCaptureResult
import android.media.Image
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import android.view.WindowManager
import androidx.core.content.ContextCompat
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import siarhei.luskanau.iot.doorbell.data.model.ImageFile
import timber.log.Timber

class CoroutineCameraRepository(
    private val context: Context,
    private val imageRepository: ImageRepository
) : BaseCameraRepository(context) {

    @SuppressLint("RestrictedApi")
    override suspend fun makeImage(
        deviceId: String,
        cameraId: String
    ): ImageFile =
        suspendCoroutine { continuation: Continuation<ImageFile> ->
            runCatching {
                openCamera(continuation, cameraId)
            }.onFailure {
                Timber.e(it)
                continuation.resumeWithException(it)
            }
        }

    @SuppressLint("MissingPermission")
    private fun openCamera(
        continuation: Continuation<ImageFile>,
        cameraId: String
    ) {
        runCatching {
            val backgroundThread = HandlerThread("CameraBackground:$cameraId")
            backgroundThread.start()
            val backgroundHandler = Handler(backgroundThread.looper)

            val cameraManager = ContextCompat.getSystemService(context, CameraManager::class.java)

            cameraManager?.openCamera(
                cameraId,
                object : CameraDevice.StateCallback() {
                    override fun onOpened(camera: CameraDevice) {
                        Timber.d("Camera ${camera.id} openCamera(): onOpened")
                        createCaptureSession(continuation, camera)
                    }

                    override fun onDisconnected(camera: CameraDevice) {
                        Timber.d("Camera ${camera.id} openCamera(): onDisconnected)")
                    }

                    override fun onClosed(camera: CameraDevice) {
                        Timber.d("Camera ${camera.id} openCamera(): onClosed}")
                    }

                    override fun onError(cameraDevice: CameraDevice, error: Int) {
                        val errorMessage = "Camera ${cameraDevice.id} openCamera(): onError:$error"
                        Timber.e(errorMessage)
                        close(cameraDevice)
                        continuation.resumeWithException(Error(errorMessage))
                    }
                },
                backgroundHandler
            )
        }.onFailure {
            Timber.e(it)
            continuation.resumeWithException(it)
        }
    }

    private fun createCaptureSession(
        continuation: Continuation<ImageFile>,
        cameraDevice: CameraDevice,
        handler: Handler? = null
    ) {
        runCatching {
            // Initialize the image processor
            val imageReader =
                ImageReader.newInstance(IMAGE_WIDTH, IMAGE_HEIGHT, ImageFormat.JPEG, MAX_IMAGES)

            @Suppress("DEPRECATION")
            cameraDevice.createCaptureSession(
                listOf(imageReader.surface),
                object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(session: CameraCaptureSession) {
                        Timber.d("Camera ${cameraDevice.id} createCaptureSession(): onConfigured")
                        capture(
                            continuation,
                            cameraDevice,
                            session,
                            imageReader
                        )
                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) {
                        val errorMessage =
                            "Camera ${cameraDevice.id} createCaptureSession(): onConfigureFailed"
                        Timber.e(errorMessage)
                        continuation.resumeWithException(Error(errorMessage))
                    }
                },
                handler
            )
        }.onFailure {
            Timber.e(it)
            close(cameraDevice)
            continuation.resumeWithException(it)
        }
    }

    private fun capture(
        continuation: Continuation<ImageFile>,
        cameraDevice: CameraDevice,
        captureSession: CameraCaptureSession,
        imageReader: ImageReader,
        handler: Handler? = null
    ) {
        runCatching {
            val captureBuilder =
                cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            captureBuilder.addTarget(imageReader.surface)
            captureBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON)

            val windowManager = ContextCompat.getSystemService(context, WindowManager::class.java)
            windowManager?.let {
                val display = windowManager.defaultDisplay
                val rotation = display.rotation
                captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS[rotation])
            }

            captureSession.capture(
                captureBuilder.build(),
                object : CameraCaptureSession.CaptureCallback() {
                    override fun onCaptureStarted(
                        session: CameraCaptureSession,
                        request: CaptureRequest,
                        timestamp: Long,
                        frameNumber: Long
                    ) {
                        Timber.d("Camera ${cameraDevice.id} capture(): onCaptureStarted")
                    }

                    override fun onCaptureProgressed(
                        session: CameraCaptureSession,
                        request: CaptureRequest,
                        partialResult: CaptureResult
                    ) {
                        Timber.d("Camera ${cameraDevice.id} capture(): onCaptureProgressed")
                    }

                    override fun onCaptureCompleted(
                        session: CameraCaptureSession,
                        request: CaptureRequest,
                        result: TotalCaptureResult
                    ) {
                        Timber.d("Camera ${cameraDevice.id} capture(): onCaptureCompleted")
                        acquireLatestImage(
                            continuation,
                            cameraDevice,
                            imageReader
                        )
                    }

                    override fun onCaptureFailed(
                        session: CameraCaptureSession,
                        request: CaptureRequest,
                        failure: CaptureFailure
                    ) {
                        Timber.d("Camera ${cameraDevice.id} capture(): onCaptureFailed")
                    }

                    override fun onCaptureSequenceCompleted(
                        session: CameraCaptureSession,
                        sequenceId: Int,
                        frameNumber: Long
                    ) {
                        Timber.d("Camera ${cameraDevice.id} capture(): onCaptureSequenceCompleted")
                    }

                    override fun onCaptureSequenceAborted(
                        session: CameraCaptureSession,
                        sequenceId: Int
                    ) {
                        Timber.d("Camera ${cameraDevice.id} capture(): onCaptureSequenceAborted")
                    }

                    override fun onCaptureBufferLost(
                        session: CameraCaptureSession,
                        request: CaptureRequest,
                        target: Surface,
                        frameNumber: Long
                    ) {
                        Timber.d("Camera ${cameraDevice.id} capture(): onCaptureBufferLost")
                    }
                },
                handler
            )
        }.onFailure {
            Timber.e(it)
            close(cameraDevice)
            continuation.resumeWithException(it)
        }
    }

    private fun acquireLatestImage(
        continuation: Continuation<ImageFile>,
        cameraDevice: CameraDevice,
        imageReader: ImageReader
    ) {
        runCatching {
            Thread.sleep(200)

            val image: Image? = imageReader.acquireLatestImage()
            Timber.d("Camera ${cameraDevice.id} acquireLatestImage image:$image")
            val imageFile: ImageFile =
                imageRepository.saveImage(image?.planes?.get(0)?.buffer, cameraDevice.id)
            close(cameraDevice)
            continuation.resume(imageFile)
        }.onFailure {
            Timber.e(it)
            close(cameraDevice)
            continuation.resumeWithException(it)
        }
    }

    private fun close(cameraDevice: CameraDevice) {
        runCatching {
            Timber.d("Camera ${cameraDevice.id} close")
            cameraDevice.close()
        }.onFailure {
            Timber.e(it)
        }
    }

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
}
