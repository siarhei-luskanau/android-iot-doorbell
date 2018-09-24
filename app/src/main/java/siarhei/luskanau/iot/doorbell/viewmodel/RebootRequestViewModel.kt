package siarhei.luskanau.iot.doorbell.viewmodel

import androidx.lifecycle.*
import io.reactivex.Flowable
import siarhei.luskanau.iot.doorbell.AppConstants
import siarhei.luskanau.iot.doorbell.data.repository.UptimeRepository
import javax.inject.Inject

class RebootRequestViewModel @Inject constructor(
        uptimeRepository: UptimeRepository
) : ViewModel() {

    val deviceIdRebootRequestTimeLiveData = MutableLiveData<Pair<String, Long>>()
    val uptimeRebootRequestUpdateLiveData: LiveData<Long> =
            Transformations.switchMap(deviceIdRebootRequestTimeLiveData) { deviceIdRebootRequestTime: Pair<String, Long> ->
                LiveDataReactiveStreams.fromPublisher(
                        Flowable.fromCallable {
                            uptimeRepository.uptimeRebootRequest(
                                    deviceId = deviceIdRebootRequestTime.first,
                                    rebootRequestTimeMillis = deviceIdRebootRequestTime.second,
                                    rebootRequestTimeString = AppConstants.DATE_FORMAT.format(deviceIdRebootRequestTime.second)
                            )
                            deviceIdRebootRequestTime.second
                        }
                )
            }

}