package siarhei.luskanau.iot.doorbell.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeRepository
import siarhei.luskanau.iot.doorbell.workmanager.dagger.AppWorkerFactory
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import javax.inject.Provider

private val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.ENGLISH)

class UptimeWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val uptimeRepository: UptimeRepository,
    private val thisDeviceRepository: ThisDeviceRepository,
    private val doorbellRepository: DoorbellRepository
) : CoroutineWorker(
    context,
    workerParams
) {

    override suspend fun doWork(): Result =
        try {

            uptimeRepository.sendIpAddressMap(
                thisDeviceRepository.doorbellId(),
                thisDeviceRepository.getIpAddressList().associate { it }
            )

            val currentTimeMillis = System.currentTimeMillis()
            uptimeRepository.uptimePing(
                thisDeviceRepository.doorbellId(),
                currentTimeMillis,
                DATE_FORMAT.format(currentTimeMillis)
            )

            doorbellRepository.sendDoorbellData(thisDeviceRepository.doorbellData())

            doorbellRepository.sendCamerasList(
                thisDeviceRepository.doorbellId(),
                thisDeviceRepository.getCamerasList()
            )

            Result.success()
        } catch (t: Throwable) {
            Timber.e(t)
            Result.failure()
        }

    class Factory @Inject constructor(
        private val appContext: Provider<Context>,
        private val uptimeRepository: Provider<UptimeRepository>,
        private val thisDeviceRepository: Provider<ThisDeviceRepository>,
        private val doorbellRepository: Provider<DoorbellRepository>
    ) : AppWorkerFactory<UptimeWorker> {
        override fun create(params: WorkerParameters): UptimeWorker {
            return UptimeWorker(
                appContext.get(),
                params,
                uptimeRepository.get(),
                thisDeviceRepository.get(),
                doorbellRepository.get()
            )
        }
    }
}