package siarhei.luskanau.iot.doorbell.viewmodel

import android.arch.lifecycle.*
import io.reactivex.Flowable
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import timber.log.Timber
import javax.inject.Inject

class ImagesViewModel @Inject constructor(
        schedulerSet: SchedulerSet,
        doorbellRepository: DoorbellRepository
) : ViewModel() {

    val deviceIdLiveData = MutableLiveData<String>()
    val imagesLiveData: LiveData<List<ImageData>> =
            Transformations.switchMap(deviceIdLiveData) { deviceId: String ->
                LiveDataReactiveStreams.fromPublisher(
                        doorbellRepository.listenImagesList(deviceId)
                                .doOnError { Timber.e(it) }
                                .onErrorResumeNext(Flowable.empty())
                                .subscribeOn(schedulerSet.io)
                                .observeOn(schedulerSet.ui)
                )
            }

}