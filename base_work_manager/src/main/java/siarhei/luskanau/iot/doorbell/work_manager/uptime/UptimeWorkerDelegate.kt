package siarhei.luskanau.iot.doorbell.work_manager.uptime

import androidx.work.Worker
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeRepository
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class UptimeWorkerDelegate @Inject constructor(
        private val uptimeRepository: UptimeRepository,
        private val thisDeviceRepository: ThisDeviceRepository,
        private val doorbellRepository: DoorbellRepository
) {

    companion object {
        private val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.ENGLISH)
    }

    fun doWork(): Worker.Result =
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

                Worker.Result.SUCCESS
            } catch (t: Throwable) {
                Timber.e(t)
                Worker.Result.FAILURE
            }
}