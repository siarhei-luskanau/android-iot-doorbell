package siarhei.luskanau.iot.doorbell.data

import java.util.Timer
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule
import kotlinx.coroutines.runBlocking
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import timber.log.Timber

class AppBackgroundServices(
    private val doorbellRepository: DoorbellRepository,
    private val thisDeviceRepository: ThisDeviceRepository,
    private val scheduleWorkManagerService: ScheduleWorkManagerService
) {

    fun startServices() {
        val period = TimeUnit.SECONDS.toMillis(3)
        Timer("AppBackgroundServices", false)
            .schedule(
                delay = 0,
                period = period
            ) {
                runCatching {
                    val requestMap: Map<String, Boolean> = runBlocking {
                        doorbellRepository.getCameraImageRequest(
                            thisDeviceRepository.doorbellId()
                        )
                    }
                    Timber.d("listenCameraImageRequest:%s", requestMap)

                    val hasImageRequest = requestMap
                        .filterValues { value -> value }
                        .isNotEmpty()

                    if (hasImageRequest) {
                        if (thisDeviceRepository.isPermissionsGranted()) {
                            scheduleWorkManagerService.cameraWorker()
                        }
                    }
                }.onFailure {
                    Timber.e(it)
                }
            }
    }
}
