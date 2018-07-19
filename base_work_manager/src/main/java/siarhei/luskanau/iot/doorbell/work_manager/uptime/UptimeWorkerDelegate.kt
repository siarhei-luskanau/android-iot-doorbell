package siarhei.luskanau.iot.doorbell.work_manager.uptime

import androidx.work.Worker
import io.reactivex.Completable
import io.reactivex.Single
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeRepository
import siarhei.luskanau.iot.doorbell.work_manager.base.WorkerDelegate
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class UptimeWorkerDelegate @Inject constructor(
        private val uptimeRepository: UptimeRepository,
        private val thisDeviceRepository: ThisDeviceRepository,
        private val doorbellRepository: DoorbellRepository
) : WorkerDelegate {

    companion object {
        private val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.ENGLISH)
    }

    override fun doWork(): Worker.Result =
            try {
                val currentTimeMillis = System.currentTimeMillis()
                Completable.mergeArray(
                        Single.just(thisDeviceRepository.getIpAddressList().associate { it })
                                .doOnError { Timber.e(it) }
                                .flatMapCompletable { ipAddressMap: Map<String, String> ->
                                    uptimeRepository.sendIpAddressMap(
                                            thisDeviceRepository.doorbellId(),
                                            ipAddressMap
                                    )
                                },
                        uptimeRepository.uptimePing(
                                thisDeviceRepository.doorbellId(),
                                currentTimeMillis,
                                DATE_FORMAT.format(currentTimeMillis)
                        )
                ).blockingGet()

                doorbellRepository.sendDoorbellData(thisDeviceRepository.doorbellData())
                        .doOnError { Timber.e(it) }
                        .blockingGet()

                Single.just(thisDeviceRepository.getCamerasList())
                        .doOnError { Timber.e(it) }
                        .flatMapCompletable { list: List<CameraData> ->
                            doorbellRepository.sendCamerasList(
                                    thisDeviceRepository.doorbellId(),
                                    list
                            )
                        }
                        .doOnError { Timber.e(it) }
                        .blockingGet()

                Worker.Result.SUCCESS
            } catch (t: Throwable) {
                Timber.e(t)
                Worker.Result.FAILURE
            }
}