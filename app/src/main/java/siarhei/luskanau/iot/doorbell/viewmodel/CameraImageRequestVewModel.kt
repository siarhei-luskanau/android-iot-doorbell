package siarhei.luskanau.iot.doorbell.viewmodel

import androidx.lifecycle.*
import io.reactivex.Flowable
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import javax.inject.Inject

class CameraImageRequestVewModel @Inject constructor(
        schedulerSet: SchedulerSet,
        doorbellRepository: DoorbellRepository
) : ViewModel() {

    val deviceIdCameraIdLiveData = MutableLiveData<Pair<String, String>>()

    val cameraImageRequestLiveData: LiveData<String> =
            Transformations.switchMap(deviceIdCameraIdLiveData) { deviceIdCameraId: Pair<String, String> ->
                LiveDataReactiveStreams.fromPublisher(
                        doorbellRepository.sendCameraImageRequest(
                                deviceId = deviceIdCameraId.first,
                                cameraId = deviceIdCameraId.second,
                                isRequested = true
                        )
                                .subscribeOn(schedulerSet.io)
                                .observeOn(schedulerSet.ui)
                                .andThen(Flowable.just(deviceIdCameraId.second))
                )
            }

}