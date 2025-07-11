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
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.core.resolutionselector.ResolutionStrategy.FALLBACK_RULE_CLOSEST_LOWER
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

    @SuppressLint("RestrictedApi", "UnsafeOptInUsageError")
    override suspend fun makeImage(doorbellId: String, cameraId: String): ImageFile =
        suspendCoroutine { continuation: Continuation<ImageFile> ->
            runCatching {
                val handler = Handler(Looper.getMainLooper())
                handler.post {
                    runCatching {
                        val cameraSelector = CameraSelector.Builder()
                            .addCameraFilter { cameras ->
                                cameras.filter { cameraInfo ->
                                    val cameraInfoInternal = cameraInfo as CameraInfoInternal
                                    cameraInfoInternal.cameraId == cameraId
                                }
                            }
                            .build()

                        val imageCapture = ImageCapture.Builder()
                            .setCaptureMode(CAPTURE_MODE_MINIMIZE_LATENCY)
                            .setResolutionSelector(
                                ResolutionSelector.Builder()
                                    .setResolutionStrategy(
                                        ResolutionStrategy(
                                            Size(480, 640),
                                            FALLBACK_RULE_CLOSEST_LOWER
                                        )
                                    )
                                    .build()
                            )
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
                                                imageRepository.saveImage(file = photoFile)
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
    }
}
