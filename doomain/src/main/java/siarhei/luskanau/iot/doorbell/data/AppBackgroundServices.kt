package siarhei.luskanau.iot.doorbell.data

import kotlinx.coroutines.runBlocking
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import timber.log.Timber
import java.util.Timer
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule

class AppBackgroundServices(
    private val doorbellRepository: DoorbellRepository,
    private val thisDeviceRepository: ThisDeviceRepository,
    private val scheduleWorkManagerService: ScheduleWorkManagerService
) {

    fun startServices() {
        val period = TimeUnit.SECONDS.toMillis(1)
        Timer("AppBackgroundServices", false).schedule(
            delay = 0,
            period = period
        ) {
            try {
                val requestMap: Map<String, Boolean>? = runBlocking {
                    doorbellRepository.getCameraImageRequest(
                        thisDeviceRepository.doorbellId()
                    )
                }
                Timber.d("listenCameraImageRequest:%s", requestMap)

                val hasImageRequest = requestMap
                    .orEmpty()
                    .filterValues { value -> value }
                    .isNotEmpty()

                if (hasImageRequest) {
                    if (thisDeviceRepository.isPermissionsGranted()) {
                        scheduleWorkManagerService.cameraWorker()
                    }
                }
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }
}