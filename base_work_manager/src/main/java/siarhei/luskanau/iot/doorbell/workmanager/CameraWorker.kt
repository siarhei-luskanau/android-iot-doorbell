package siarhei.luskanau.iot.doorbell.workmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.runBlocking
import siarhei.luskanau.iot.doorbell.data.repository.CameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import siarhei.luskanau.iot.doorbell.workmanager.dagger.AppWorkerFactory
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

class CameraWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val thisDeviceRepository: ThisDeviceRepository,
    private val doorbellRepository: DoorbellRepository,
    private val cameraRepository: CameraRepository
) : Worker(
    context,
    workerParams
) {

    override fun doWork(): Result =
        try {
            doorbellRepository.getCameraImageRequest(thisDeviceRepository.doorbellId())
                .filterValues { value -> value }
                .onEach { (cameraId, _) ->
                    doorbellRepository.sendCameraImageRequest(
                        deviceId = thisDeviceRepository.doorbellId(),
                        cameraId = cameraId,
                        isRequested = false
                    )

                    runBlocking {
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
                }

            Result.success()
        } catch (t: Throwable) {
            Timber.e(t)
            Result.failure()
        }

    class Factory @Inject constructor(
        // left out params: WorkerParameters for the create() method
        private val appContext: Provider<Context>,
        private val thisDeviceRepository: Provider<ThisDeviceRepository>,
        private val doorbellRepository: Provider<DoorbellRepository>,
        private val cameraRepository: Provider<CameraRepository>
    ) : AppWorkerFactory<CameraWorker> {
        override fun create(params: WorkerParameters): CameraWorker {
            return CameraWorker(
                appContext.get(),
                params,
                thisDeviceRepository.get(),
                doorbellRepository.get(),
                cameraRepository.get()
            )
        }
    }
}