package siarhei.luskanau.iot.doorbell.data.repository

import androidx.camera.core.CameraX
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureConfig
import androidx.lifecycle.ProcessLifecycleOwner
import siarhei.luskanau.iot.doorbell.data.model.ImageFile
import java.io.File
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AndroidCameraRepository(
    private val imageRepository: ImageRepository
) : CameraRepository {
    override suspend fun makeImage(
        deviceId: String,
        cameraId: String
    ): ImageFile =
        suspendCoroutine { continuation: Continuation<ImageFile> ->
            val imageCaptureConfig = ImageCaptureConfig.Builder().build()
            val imageCapture = ImageCapture(imageCaptureConfig)
            CameraX.bindToLifecycle(ProcessLifecycleOwner.get(), imageCapture)

            imageCapture.takePicture(imageRepository.prepareFile(cameraId),
                object : ImageCapture.OnImageSavedListener {
                    override fun onImageSaved(file: File) {
                        CameraX.unbind(imageCapture)
                        continuation.resume(imageRepository.saveImage(file))
                    }

                    override fun onError(
                        useCaseError: ImageCapture.UseCaseError,
                        message: String,
                        cause: Throwable?
                    ) {
                        continuation.resumeWithException(cause ?: Error(message))
                    }
                }
            )
        }
}