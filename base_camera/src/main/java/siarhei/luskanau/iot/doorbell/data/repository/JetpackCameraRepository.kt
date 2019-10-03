package siarhei.luskanau.iot.doorbell.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.util.Size
import androidx.camera.core.CameraX
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.CaptureMode
import androidx.camera.core.ImageCaptureConfig
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
                try {
                    val imageCaptureConfig = ImageCaptureConfig.Builder()
                            .setCameraIdFilter(IdCameraIdFilter(cameraId))
                            .setCaptureMode(CaptureMode.MIN_LATENCY)
                            .setTargetResolution(Size(480, 640))
                            .build()

                    val imageCapture = ImageCapture(imageCaptureConfig)

                    CameraX.bindToLifecycle(ProcessLifecycleOwner.get(), imageCapture)

                    // TODO use ImageAnalysis to check if camera is ready
                    Thread.sleep(1000)

                    imageCapture.takePicture(imageRepository.prepareFile(cameraId),
                            object : ImageCapture.OnImageSavedListener {
                                override fun onImageSaved(file: File) {
                                    CameraX.unbind(imageCapture)
                                    continuation.resume(imageRepository.saveImage(file))
                                }

                                override fun onError(
                                    imageCaptureError: ImageCapture.ImageCaptureError,
                                    message: String,
                                    cause: Throwable?
                                ) {
                                    continuation.resumeWithException(cause ?: Error(message))
                                }
                            }
                    )
                } catch (t: Throwable) {
                    continuation.resumeWithException(t)
                }
            }
}
