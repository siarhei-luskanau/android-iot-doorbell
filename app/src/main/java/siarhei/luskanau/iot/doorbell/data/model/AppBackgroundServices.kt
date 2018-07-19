package siarhei.luskanau.iot.doorbell.data.model

import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import io.reactivex.Completable
import siarhei.luskanau.iot.doorbell.CameraService
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import timber.log.Timber
import javax.inject.Inject

class AppBackgroundServices @Inject constructor(
        private val schedulerSet: SchedulerSet,
        private val doorbellRepository: DoorbellRepository,
        private val thisDeviceRepository: ThisDeviceRepository,
        val gson: Gson,
        val context: Context
) {

    fun startServices() {
        doorbellRepository.listenCameraImageRequest(thisDeviceRepository.doorbellId())
                .flatMapCompletable { requestMap: Map<String, Boolean>? ->
                    Timber.d("listenCameraImageRequest:%s", gson.toJson(requestMap))
                    val map: Map<String, Boolean> = requestMap.orEmpty()
                            .filter { entry: Map.Entry<String, Boolean> ->
                                entry.value
                            }

                    Completable.fromAction {
                        if (map.isNotEmpty()) {
                            context.startService(Intent(context, CameraService::class.java))
                        }
                    }

                }
                .doOnError { Timber.e(it) }
                .subscribeOn(schedulerSet.io)
                .observeOn(schedulerSet.computation)
                .subscribe()
    }

}