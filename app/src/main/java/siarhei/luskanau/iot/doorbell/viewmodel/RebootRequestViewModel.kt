package siarhei.luskanau.iot.doorbell.viewmodel

import androidx.lifecycle.*
import io.reactivex.Flowable
import siarhei.luskanau.iot.doorbell.AppConstants
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.repository.UptimeRepository
import javax.inject.Inject

class RebootRequestViewModel @Inject constructor(
        schedulerSet: SchedulerSet,
        uptimeRepository: UptimeRepository
) : ViewModel() {

    val deviceIdRebootRequestTimeLiveData = MutableLiveData<Pair<String, Long>>()
    val uptimeRebootRequestUpdateLiveData: LiveData<Long> =
            Transformations.switchMap(deviceIdRebootRequestTimeLiveData) { deviceIdRebootRequestTime: Pair<String, Long> ->
                LiveDataReactiveStreams.fromPublisher(
                        uptimeRepository.uptimeRebootRequest(
                                deviceId = deviceIdRebootRequestTime.first,
                                rebootRequestTimeMillis = deviceIdRebootRequestTime.second,
                                rebootRequestTimeString = AppConstants.DATE_FORMAT.format(deviceIdRebootRequestTime.second)
                        )
                                .subscribeOn(schedulerSet.io)
                                .observeOn(schedulerSet.ui)
                                .andThen(Flowable.just(deviceIdRebootRequestTime.second))
                )
            }

}