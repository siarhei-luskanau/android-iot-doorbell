package siarhei.luskanau.iot.doorbell.data.model

import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import siarhei.luskanau.iot.doorbell.CameraService
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AppBackgroundServices @Inject constructor(
        private val schedulerSet: SchedulerSet,
        private val doorbellRepository: DoorbellRepository,
        private val thisDeviceRepository: ThisDeviceRepository,
        val gson: Gson,
        val context: Context
) {

    companion object {
        private const val initial_delay: Long = 0
        private const val period: Long = 10
        private val unit = TimeUnit.SECONDS
    }

    fun startServices() {
        sendDoorbellData()
        sendCamerasList()
        listenCameraImageRequest()
    }

    private fun sendDoorbellData() {
        Flowable
                .interval(initial_delay, period, unit, schedulerSet.computation)
                .flatMapCompletable {
                    doorbellRepository.sendDoorbellData(thisDeviceRepository.doorbellData())
                }
                .doOnError { Timber.e(it) }
                .subscribeOn(schedulerSet.io)
                .observeOn(schedulerSet.computation)
                .subscribe()
    }

    private fun sendCamerasList() {
        Flowable
                .interval(initial_delay, period, unit, schedulerSet.computation)
                .flatMapCompletable {
                    Single.just(thisDeviceRepository.getCamerasList())
                            .doOnError { Timber.e(it) }
                            .flatMapCompletable { list: List<CameraData> ->
                                doorbellRepository.sendCamerasList(
                                        thisDeviceRepository.doorbellId(),
                                        list
                                )
                            }
                }
                .doOnError { Timber.e(it) }
                .subscribeOn(schedulerSet.io)
                .observeOn(schedulerSet.computation)
                .subscribe()
    }

    private fun listenCameraImageRequest() {
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