package siarhei.luskanau.iot.doorbell.workmanager

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import siarhei.luskanau.iot.doorbell.data.repository.CameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ImageRepository
import siarhei.luskanau.iot.doorbell.data.repository.ImageSenderRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeRepository
import timber.log.Timber

class DefaultWorkerFactory(
    private val thisDeviceRepository: () -> ThisDeviceRepository,
    private val doorbellRepository: () -> DoorbellRepository,
    private val imageSenderRepository: () -> ImageSenderRepository,
    private val cameraRepository: () -> CameraRepository,
    private val uptimeRepository: () -> UptimeRepository,
    private val imageRepository: () -> ImageRepository,
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters,
    ): ListenableWorker? =
        runCatching {
            Timber.e("DefaultWorkerFactory:createWorker:$workerClassName")
            when (workerClassName) {
                CameraWorker::class.java.name -> CameraWorker(
                    context = appContext,
                    workerParams = workerParameters,
                    thisDeviceRepository = thisDeviceRepository.invoke(),
                    doorbellRepository = doorbellRepository.invoke(),
                    imageSenderRepository = imageSenderRepository.invoke(),
                    cameraRepository = cameraRepository.invoke(),
                    imageRepository = imageRepository.invoke(),
                )

                UptimeWorker::class.java.name -> UptimeWorker(
                    context = appContext,
                    workerParams = workerParameters,
                    uptimeRepository = uptimeRepository.invoke(),
                    thisDeviceRepository = thisDeviceRepository.invoke(),
                    doorbellRepository = doorbellRepository.invoke(),
                )

                else -> null
            }
        }.onFailure {
            Timber.e(it, "DefaultWorkerFactory:createWorker:$workerClassName")
        }.getOrNull()
}
