package siarhei.luskanau.iot.doorbell.workmanager

import android.content.Context
import android.net.Uri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dev.gitlive.firebase.storage.File
import siarhei.luskanau.iot.doorbell.data.repository.CameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ImageSenderRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import timber.log.Timber

class CameraWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val thisDeviceRepository: ThisDeviceRepository,
    private val doorbellRepository: DoorbellRepository,
    private val imageSenderRepository: ImageSenderRepository,
    private val cameraRepository: CameraRepository
) : CoroutineWorker(
    context,
    workerParams
) {

    override suspend fun doWork(): Result = runCatching {
        doorbellRepository.getCameraImageRequest(
            thisDeviceRepository.doorbellId()
        )
            .filterValues { value -> value }
            .onEach { (cameraId, _) ->
                doorbellRepository.sendCameraImageRequest(
                    doorbellId = thisDeviceRepository.doorbellId(),
                    cameraId = cameraId,
                    isRequested = false
                )

                cameraRepository
                    .makeImage(
                        doorbellId = thisDeviceRepository.doorbellId(),
                        cameraId = cameraId
                    )
                    .also { imageFile ->
                        val uri = Uri.fromFile(imageFile.path?.let { java.io.File(it) })
                        imageSenderRepository.sendImage(
                            doorbellId = thisDeviceRepository.doorbellId(),
                            cameraId = cameraId,
                            file = File(uri)
                        )
                    }
            }

        Result.success()
    }.onFailure {
        Timber.e(it)
        Result.failure()
    }.getOrDefault(Result.failure())
}
