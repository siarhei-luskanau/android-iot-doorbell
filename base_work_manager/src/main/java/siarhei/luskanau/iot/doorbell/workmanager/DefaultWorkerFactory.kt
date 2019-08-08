package siarhei.luskanau.iot.doorbell.workmanager

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import siarhei.luskanau.iot.doorbell.data.repository.CameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeRepository
import timber.log.Timber

class DefaultWorkerFactory(
    private val thisDeviceRepository: ThisDeviceRepository,
    private val doorbellRepository: DoorbellRepository,
    private val cameraRepository: CameraRepository,
    private val uptimeRepository: UptimeRepository
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? =
        try {
            Timber.e("DefaultWorkerFactory:createWorker:$workerClassName")
            when (workerClassName) {

                CameraWorker::class.java.name -> CameraWorker(
                    context = appContext,
                    workerParams = workerParameters,
                    thisDeviceRepository = thisDeviceRepository,
                    doorbellRepository = doorbellRepository,
                    cameraRepository = cameraRepository
                )

                UptimeWorker::class.java.name -> UptimeWorker(
                    context = appContext,
                    workerParams = workerParameters,
                    uptimeRepository = uptimeRepository,
                    thisDeviceRepository = thisDeviceRepository,
                    doorbellRepository = doorbellRepository
                )

                else -> null
            }
        } catch (throwable: Throwable) {
            Timber.e(throwable, "DefaultWorkerFactory:createWorker:$workerClassName")
            null
        }
}
