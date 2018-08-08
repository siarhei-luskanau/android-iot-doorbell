package siarhei.luskanau.iot.doorbell.viewmodel

import androidx.lifecycle.*
import io.reactivex.Flowable
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import timber.log.Timber
import javax.inject.Inject

class CamerasViewModel @Inject constructor(
        schedulerSet: SchedulerSet,
        doorbellRepository: DoorbellRepository
) : ViewModel() {

    val deviceIdLiveData = MutableLiveData<String>()
    val camerasLiveData: LiveData<List<CameraData>> =
            Transformations.switchMap(deviceIdLiveData)
            { deviceId: String ->
                LiveDataReactiveStreams.fromPublisher(
                        doorbellRepository.listenCamerasList(deviceId)
                                .doOnError { Timber.e(it) }
                                .onErrorResumeNext(Flowable.empty())
                                .subscribeOn(schedulerSet.io)
                                .observeOn(schedulerSet.ui)
                )
            }

}