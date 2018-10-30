package siarhei.luskanau.iot.doorbell.work_manager.camera

import androidx.work.ListenableWorker
import siarhei.luskanau.iot.doorbell.data.repository.CameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import timber.log.Timber
import javax.inject.Inject

class CameraWorkerDelegate @Inject constructor(
        private val thisDeviceRepository: ThisDeviceRepository,
        private val doorbellRepository: DoorbellRepository,
        private val cameraRepository: CameraRepository
) {

    fun doWork(): ListenableWorker.Result =
            try {
                doorbellRepository.getCameraImageRequest(thisDeviceRepository.doorbellId())
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
                                    .blockingIterable()
                                    .onEach { imageFile ->
                                        doorbellRepository.sendImage(
                                                deviceId = thisDeviceRepository.doorbellId(),
                                                cameraId = cameraId,
                                                imageFile = imageFile
                                        )
                                    }
                        }

                ListenableWorker.Result.SUCCESS
            } catch (t: Throwable) {
                Timber.e(t)
                ListenableWorker.Result.FAILURE
            }

}