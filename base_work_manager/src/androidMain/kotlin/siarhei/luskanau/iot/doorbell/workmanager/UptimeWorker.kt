package siarhei.luskanau.iot.doorbell.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeRepository
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Locale

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
        runCatching {
            uptimeRepository.sendIpAddressMap(
                thisDeviceRepository.doorbellId(),
                thisDeviceRepository.getIpAddressList()
            )

            val currentTimeMillis = System.currentTimeMillis()
            uptimeRepository.uptimePing(
                thisDeviceRepository.doorbellId(),
                currentTimeMillis,
                DATE_FORMAT.format(currentTimeMillis)
            )

            doorbellRepository.sendDoorbellData(
                thisDeviceRepository.doorbellData()
            )

            doorbellRepository.sendCamerasList(
                thisDeviceRepository.doorbellId(),
                thisDeviceRepository.getCamerasList()
            )

            Result.success()
        }.onFailure {
            Timber.e(it)
            Result.failure()
        }.getOrDefault(Result.failure())
}
