package siarhei.luskanau.iot.doorbell.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraSelector.LENS_FACING_BACK
import androidx.camera.core.CameraSelector.LENS_FACING_FRONT
import androidx.camera.core.CameraX
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY
import androidx.camera.core.impl.utils.executor.CameraXExecutors
import androidx.lifecycle.ProcessLifecycleOwner
import java.io.File
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import siarhei.luskanau.iot.doorbell.data.model.ImageFile

class JetpackCameraRepository(
    context: Context,
    private val imageRepository: ImageRepository
) : BaseCameraRepository(context) {

    @SuppressLint("RestrictedApi")
    override suspend fun makeImage(
        deviceId: String,
        cameraId: String
    ): ImageFile =
        suspendCoroutine { continuation: Continuation<ImageFile> ->
            runCatching {
                val handler = Handler(Looper.getMainLooper())
                handler.post {
                    runCatching {
                        val cameraSelector = CameraSelector.Builder()
                            .requireLensFacing(
                                when (cameraId) {
                                    CameraX.getCameraWithLensFacing(LENS_FACING_BACK) -> LENS_FACING_BACK

                                    CameraX.getCameraWithLensFacing(LENS_FACING_FRONT) -> LENS_FACING_FRONT

                                    else -> LENS_FACING_BACK
                                }
                            )
                            .build()

                        val imageCapture = ImageCapture.Builder()
                            .setCaptureMode(CAPTURE_MODE_MINIMIZE_LATENCY)
                            .setTargetResolution(Size(480, 640))
                            .build()

                        CameraX.bindToLifecycle(ProcessLifecycleOwner.get(), cameraSelector)

                        // TODO use ImageAnalysis to check if camera is ready
                        Thread.sleep(1000)

                        imageCapture.takePicture(
                            imageRepository.prepareFile(cameraId),
                            CameraXExecutors.ioExecutor(),
                            object : ImageCapture.OnImageSavedCallback {

                                override fun onImageSaved(file: File) {
                                    handler.post {
                                        runCatching {
                                            CameraX.unbind(imageCapture)
                                            continuation.resume(imageRepository.saveImage(file))
                                        }.onFailure {
                                            continuation.resumeWithException(it)
                                        }
                                    }
                                }

                                override fun onError(
                                    imageCaptureError: Int,
                                    message: String,
                                    cause: Throwable?
                                ) {
                                    continuation.resumeWithException(cause ?: Error(message))
                                }
                            }
                        )
                    }.onFailure {
                        continuation.resumeWithException(it)
                    }
                }
            }.onFailure {
                continuation.resumeWithException(it)
            }
        }
}
