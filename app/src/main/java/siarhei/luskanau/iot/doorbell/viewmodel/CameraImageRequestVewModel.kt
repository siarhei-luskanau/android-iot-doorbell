package siarhei.luskanau.iot.doorbell.viewmodel

import io.reactivex.Completable
import io.reactivex.rxkotlin.subscribeBy
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import timber.log.Timber
import javax.inject.Inject

class CameraImageRequestVewModel @Inject constructor(
    private val schedulerSet: SchedulerSet,
    private val doorbellRepository: DoorbellRepository
) : BaseViewModel() {

    fun requestCameraImage(deviceId: String, cameraId: String) {
        Completable.fromAction {
            doorbellRepository.sendCameraImageRequest(
                    deviceId = deviceId,
                    cameraId = cameraId,
                    isRequested = true
            )
        }
                .subscribeOn(schedulerSet.io)
                .observeOn(schedulerSet.ui)
                .subscribeBy(
                        onComplete = {},
                        onError = { Timber.e(it) }
                )
                .also { disposables.add(it) }
    }
}