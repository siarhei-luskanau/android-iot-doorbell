package siarhei.luskanau.iot.doorbell.ui.images

import android.arch.lifecycle.*
import io.reactivex.Flowable
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import timber.log.Timber
import javax.inject.Inject

class ImagesViewModel @Inject constructor(
        schedulerSet: SchedulerSet,
        doorbellRepository: DoorbellRepository
) : ViewModel() {

    val deviceIdLiveData = MutableLiveData<String>()
    val camerasLiveData: LiveData<List<CameraData>>
    val imagesLiveData: LiveData<List<ImageData>>
    val cameraIdLiveData = MutableLiveData<String>()
    val cameraImageRequestLiveData: LiveData<String>

    init {
        camerasLiveData = Transformations.switchMap(deviceIdLiveData) { deviceId: String ->
            LiveDataReactiveStreams.fromPublisher(
                    doorbellRepository.listenCamerasList(deviceId)
                            .doOnError { Timber.e(it) }
                            .onErrorResumeNext(Flowable.empty())
                            .subscribeOn(schedulerSet.io)
                            .observeOn(schedulerSet.ui)
            )
        }

        imagesLiveData = Transformations.switchMap(deviceIdLiveData) { deviceId: String ->
            LiveDataReactiveStreams.fromPublisher(
                    doorbellRepository.listenImagesList(deviceId)
                            .doOnError { Timber.e(it) }
                            .onErrorResumeNext(Flowable.empty())
                            .subscribeOn(schedulerSet.io)
                            .observeOn(schedulerSet.ui)
            )
        }

        cameraImageRequestLiveData = Transformations.switchMap(cameraIdLiveData) { cameraId: String ->
            LiveDataReactiveStreams.fromPublisher(
                    doorbellRepository.sendCameraImageRequest(
                            deviceId = deviceIdLiveData.value.orEmpty(),
                            cameraId = cameraId,
                            isRequested = true
                    )
                            .subscribeOn(schedulerSet.io)
                            .observeOn(schedulerSet.ui)
                            .andThen(Flowable.just(cameraId))
            )
        }
    }

}