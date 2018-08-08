package siarhei.luskanau.iot.doorbell.data.model

import io.reactivex.Completable
import siarhei.luskanau.iot.doorbell.AppConstants.DATE_FORMAT
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.UptimeService
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeRepository
import timber.log.Timber
import javax.inject.Inject

class RxUptimeService @Inject constructor(
        private val schedulerSet: SchedulerSet,
        private val uptimeRepository: UptimeRepository,
        private val thisDeviceRepository: ThisDeviceRepository
) : UptimeService {

    override fun startUptimeNotifications() {
        uptimeStartupNotifications()
        listenRebootRequest()
    }

    private fun uptimeStartupNotifications() {
        val currentTimeMillis = System.currentTimeMillis()
        uptimeRepository
                .uptimeStartup(
                        thisDeviceRepository.doorbellId(),
                        currentTimeMillis,
                        DATE_FORMAT.format(currentTimeMillis)
                )
                .subscribeOn(schedulerSet.io)
                .observeOn(schedulerSet.computation)
                .subscribe {
                    Timber.d("onComplete:uptimeStartupNotifications")
                }
    }

    private fun listenRebootRequest() {
        uptimeRepository.listenUptime(thisDeviceRepository.doorbellId())
                .flatMapCompletable { uptime: Uptime ->
                    var isNeedReboot = false

                    uptime.rebootRequestTimeMillis?.let { rebootRequestTimeMillis ->

                        uptime.startupTimeMillis?.let { startupTimeMillis ->
                            isNeedReboot = rebootRequestTimeMillis > startupTimeMillis
                        } ?: run {
                            isNeedReboot = true
                        }

                        uptime.rebootingTimeMillis?.let { rebootingTimeMillis ->
                            isNeedReboot = rebootingTimeMillis < rebootRequestTimeMillis
                        }

                    }

                    val currentTimeMillis = System.currentTimeMillis()
                    if (isNeedReboot) uptimeRepository
                            .uptimeRebooting(
                                    thisDeviceRepository.doorbellId(),
                                    currentTimeMillis,
                                    DATE_FORMAT.format(currentTimeMillis)
                            )
                            .andThen {
                                Timber.d("reboot")
                                thisDeviceRepository.reboot()
                            }
                    else Completable.complete()
                }
                .doOnError { Timber.e(it) }
                .subscribeOn(schedulerSet.io)
                .observeOn(schedulerSet.computation)
                .subscribe()
    }

    override fun cameraWorker() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
