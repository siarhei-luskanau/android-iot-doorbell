package siarhei.luskanau.iot.doorbell.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.impl.CameraInfoInternal
import androidx.camera.core.impl.utils.executor.CameraXExecutors
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.lifecycle.ProcessLifecycleOwner
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import siarhei.luskanau.iot.doorbell.data.model.ImageFile

class JetpackCameraRepository(
    private val context: Context,
    private val imageRepository: ImageRepository
) : BaseCameraRepository(context) {

    @SuppressLint("RestrictedApi", "UnsafeExperimentalUsageError")
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
                            .addCameraFilter { cameras ->
                                val filteredCameras = cameras.filter { camera ->
                                    val cameraInfo = camera.cameraInfo as CameraInfoInternal
                                    cameraInfo.cameraId == cameraId
                                }
                                LinkedHashSet(filteredCameras)
                            }
                            .build()

                        val imageCapture = ImageCapture.Builder()
                            .setCaptureMode(CAPTURE_MODE_MINIMIZE_LATENCY)
                            .setTargetResolution(Size(WIDTH, HEIGHT))
                            .build()

                        val processCameraProvider = ProcessCameraProvider.getInstance(context).get()
                        processCameraProvider.bindToLifecycle(
                            ProcessLifecycleOwner.get(),
                            cameraSelector,
                            imageCapture
                        )

                        // TODO use ImageAnalysis to check if camera is ready
                        Thread.sleep(SLEEP)

                        val photoFile = imageRepository.prepareFile(cameraId)
                        imageCapture.takePicture(
                            ImageCapture.OutputFileOptions.Builder(photoFile).build(),
                            CameraXExecutors.ioExecutor(),
                            object : ImageCapture.OnImageSavedCallback {

                                override fun onImageSaved(
                                    outputFileResults: ImageCapture.OutputFileResults
                                ) {
                                    handler.post {
                                        runCatching {
                                            processCameraProvider.unbind(imageCapture)
                                            continuation.resume(
                                                imageRepository.saveImage(
                                                    imageUri = outputFileResults.savedUri,
                                                    file = photoFile
                                                )
                                            )
                                        }.onFailure {
                                            continuation.resumeWithException(it)
                                        }
                                    }
                                }

                                override fun onError(exception: ImageCaptureException) {
                                    continuation.resumeWithException(exception)
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

    companion object {
        private const val SLEEP = 1_000L
        private const val WIDTH = 480
        private const val HEIGHT = 640
    }
}
