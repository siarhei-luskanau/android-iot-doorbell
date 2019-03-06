package siarhei.luskanau.iot.doorbell.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import siarhei.luskanau.iot.doorbell.AppConstants
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.repository.UptimeRepository
import timber.log.Timber
import javax.inject.Inject

class RebootRequestViewModel @Inject constructor(
    private val schedulerSet: SchedulerSet,
    private val uptimeRepository: UptimeRepository
) : BaseViewModel() {

    fun rebootDevice(deviceId: String, currentTime: Long) {
        viewModelScope.launch(schedulerSet.ioCoroutineContext) {
            try {
                uptimeRepository.uptimeRebootRequest(
                        deviceId = deviceId,
                        rebootRequestTimeMillis = currentTime,
                        rebootRequestTimeString = AppConstants.DATE_FORMAT.format(currentTime)
                )
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }.also { jobs.add(it) }
    }
}