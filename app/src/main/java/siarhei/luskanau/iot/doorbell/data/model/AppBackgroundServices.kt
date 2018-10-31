package siarhei.luskanau.iot.doorbell.data.model

import android.content.Context
import android.content.Intent
import io.reactivex.Completable
import io.reactivex.Flowable
import siarhei.luskanau.iot.doorbell.PermissionActivity
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.UptimeService
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AppBackgroundServices @Inject constructor(
    private val schedulerSet: SchedulerSet,
    private val doorbellRepository: DoorbellRepository,
    private val thisDeviceRepository: ThisDeviceRepository,
    private val uptimeService: UptimeService,
    val context: Context
) {

    fun startServices() {
        Flowable.interval(0, 1, TimeUnit.SECONDS)
                .flatMapCompletable {
                    Completable.fromAction {
                        val requestMap: Map<String, Boolean>? = doorbellRepository
                                .getCameraImageRequest(
                                        thisDeviceRepository.doorbellId()
                                )
                        Timber.d("listenCameraImageRequest:%s", requestMap)

                        val hasImageRequest = requestMap
                                .orEmpty()
                                .filterValues { value -> value }
                                .isNotEmpty()

                        if (hasImageRequest) {
                            if (thisDeviceRepository.isPermissionsGranted()) {
                                uptimeService.cameraWorker()
                            } else {
                                Timber.d("Permissions is not granted")
                                context.startActivity(Intent(context, PermissionActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                            }
                        }
                    }
                }
                .doOnError { Timber.e(it) }
                .subscribeOn(schedulerSet.io)
                .observeOn(schedulerSet.computation)
                .subscribe()
    }
}