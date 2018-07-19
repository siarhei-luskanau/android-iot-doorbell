package siarhei.luskanau.iot.doorbell.work_manager

import androidx.work.*
import siarhei.luskanau.iot.doorbell.data.UptimeService
import siarhei.luskanau.iot.doorbell.work_manager.WorkManagerConstants.REPEAT_INTERVAL
import siarhei.luskanau.iot.doorbell.work_manager.WorkManagerConstants.REPEAT_INTERVAL_TIME_UNIT
import siarhei.luskanau.iot.doorbell.work_manager.WorkManagerConstants.UPTIME_WORK_NAME
import siarhei.luskanau.iot.doorbell.work_manager.uptime.UptimeWorker

class DefaultUptimeService : UptimeService {

    override fun startUptimeNotifications() {

        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        val request = PeriodicWorkRequest.Builder(
                UptimeWorker::class.java,
                REPEAT_INTERVAL,
                REPEAT_INTERVAL_TIME_UNIT
        )
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
                UPTIME_WORK_NAME,
                ExistingPeriodicWorkPolicy.REPLACE,
                request
        )
    }

}