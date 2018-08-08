package siarhei.luskanau.iot.doorbell.work_manager.camera

import androidx.work.Worker
import io.reactivex.Completable
import siarhei.luskanau.iot.doorbell.data.model.ImageFile
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

    fun doWork(): Worker.Result =
            try {
                doorbellRepository.listenCameraImageRequest(thisDeviceRepository.doorbellId())
                        .firstOrError()
                        .flatMapCompletable { requestMap: Map<String, Boolean>? ->
                            Completable.concat(requestMap.orEmpty()
                                    .filter { entry: Map.Entry<String, Boolean> ->
                                        entry.value
                                    }
                                    .map {
                                        makeAndSendImage(it.key)
                                    }
                            )
                        }
                        .blockingGet()

                Worker.Result.SUCCESS
            } catch (t: Throwable) {
                Timber.e(t)
                Worker.Result.FAILURE
            }

    private fun makeAndSendImage(cameraId: String): Completable =
            doorbellRepository.sendCameraImageRequest(
                    deviceId = thisDeviceRepository.doorbellId(),
                    cameraId = cameraId,
                    isRequested = false
            ).andThen(
                    cameraRepository
                            .makeImage(
                                    deviceId = thisDeviceRepository.doorbellId(),
                                    cameraId = cameraId
                            )
                            .filter { it.size ?: 0 > 0 }
                            .flatMap { imageFile: ImageFile ->
                                doorbellRepository.sendImage(
                                        deviceId = thisDeviceRepository.doorbellId(),
                                        cameraId = cameraId,
                                        imageFile = imageFile
                                )
                                        .toObservable<Nothing>()
                            }
                            .ignoreElements())

}