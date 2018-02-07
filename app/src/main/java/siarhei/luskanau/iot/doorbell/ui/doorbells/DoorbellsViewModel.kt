package siarhei.luskanau.iot.doorbell.ui.doorbells

import android.arch.lifecycle.*
import io.reactivex.Flowable
import io.reactivex.Single
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import timber.log.Timber
import javax.inject.Inject

class DoorbellsViewModel @Inject constructor(
        schedulerSet: SchedulerSet,
        doorbellRepository: DoorbellRepository,
        thisDeviceRepository: ThisDeviceRepository
) : ViewModel() {

    val camerasLiveData: LiveData<List<CameraData>>
    val doorbellsLiveData: LiveData<List<DoorbellData>>
    val cameraIdLiveData = MutableLiveData<String>()
    val cameraImageRequestLiveData: LiveData<String>

    init {
        camerasLiveData = LiveDataReactiveStreams.fromPublisher(
                thisDeviceRepository.getCamerasList()
                        .doOnError { Timber.e(it) }
                        .onErrorResumeNext(Single.just(emptyList()))
                        .subscribeOn(schedulerSet.io)
                        .observeOn(schedulerSet.ui)
                        .toFlowable()
        )

        doorbellsLiveData = LiveDataReactiveStreams.fromPublisher(
                doorbellRepository.listenDoorbellsList()
                        .doOnError { Timber.e(it) }
                        .onErrorResumeNext(Flowable.empty())
                        .subscribeOn(schedulerSet.io)
                        .observeOn(schedulerSet.ui)
        )

        cameraImageRequestLiveData = Transformations.switchMap(cameraIdLiveData) { cameraId: String ->
            LiveDataReactiveStreams.fromPublisher(
                    doorbellRepository.sendCameraImageRequest(
                            deviceId = thisDeviceRepository.doorbellId(),
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
