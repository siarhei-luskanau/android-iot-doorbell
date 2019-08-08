package siarhei.luskanau.iot.doorbell.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import siarhei.luskanau.iot.doorbell.data.repository.CameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import timber.log.Timber

class CameraWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val thisDeviceRepository: ThisDeviceRepository,
    private val doorbellRepository: DoorbellRepository,
    private val cameraRepository: CameraRepository
) : CoroutineWorker(
    context,
    workerParams
) {

    override suspend fun doWork(): Result =
        try {
            doorbellRepository.getCameraImageRequest(
                thisDeviceRepository.doorbellId()
            )
                .filterValues { value -> value }
                .onEach { (cameraId, _) ->
                    doorbellRepository.sendCameraImageRequest(
                        deviceId = thisDeviceRepository.doorbellId(),
                        cameraId = cameraId,
                        isRequested = false
                    )

                    cameraRepository
                        .makeImage(
                            deviceId = thisDeviceRepository.doorbellId(),
                            cameraId = cameraId
                        )
                        .also { imageFile ->
                            doorbellRepository.sendImage(
                                deviceId = thisDeviceRepository.doorbellId(),
                                cameraId = cameraId,
                                imageFile = imageFile
                            )
                        }
                }

            Result.success()
        } catch (t: Throwable) {
            Timber.e(t)
            Result.failure()
        }
}
