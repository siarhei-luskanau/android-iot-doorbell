package siarhei.luskanau.iot.doorbell.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import siarhei.luskanau.iot.doorbell.data.repository.CameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ImageRepository
import siarhei.luskanau.iot.doorbell.data.repository.ImageSenderRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import timber.log.Timber

class CameraWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val thisDeviceRepository: ThisDeviceRepository,
    private val doorbellRepository: DoorbellRepository,
    private val imageSenderRepository: ImageSenderRepository,
    private val cameraRepository: CameraRepository,
    private val imageRepository: ImageRepository,
) : CoroutineWorker(
    context,
    workerParams,
) {

    override suspend fun doWork(): Result =
        runCatching {
            doorbellRepository.getCameraImageRequest(
                thisDeviceRepository.doorbellId(),
            )
                .filterValues { value -> value }
                .onEach { (cameraId, _) ->
                    doorbellRepository.sendCameraImageRequest(
                        doorbellId = thisDeviceRepository.doorbellId(),
                        cameraId = cameraId,
                        isRequested = false,
                    )

                    cameraRepository
                        .makeImage(
                            doorbellId = thisDeviceRepository.doorbellId(),
                            cameraId = cameraId,
                        )
                        .also { imageFile ->
                            imageSenderRepository.sendImage(
                                doorbellId = thisDeviceRepository.doorbellId(),
                                cameraId = cameraId,
                                imageInputStream = imageRepository.openInputStream(
                                    imageFile.path.orEmpty(),
                                ),
                            )
                        }
                }

            Result.success()
        }.onFailure {
            Timber.e(it)
            Result.failure()
        }.getOrDefault(Result.failure())
}
