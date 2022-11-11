package siarhei.luskanau.iot.doorbell.workmanager

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import siarhei.luskanau.iot.doorbell.data.ScheduleWorkManagerService
import siarhei.luskanau.iot.doorbell.workmanager.WorkManagerConstants.REPEAT_INTERVAL
import siarhei.luskanau.iot.doorbell.workmanager.WorkManagerConstants.REPEAT_INTERVAL_TIME_UNIT
import siarhei.luskanau.iot.doorbell.workmanager.WorkManagerConstants.UPTIME_WORK_NAME

class DefaultScheduleWorkManagerService(
    private val workManager: () -> WorkManager
) : ScheduleWorkManagerService {

    override fun startUptimeNotifications() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = PeriodicWorkRequestBuilder<UptimeWorker>(
            REPEAT_INTERVAL,
            REPEAT_INTERVAL_TIME_UNIT
        )
            .setConstraints(constraints)
            .build()

        workManager().enqueueUniquePeriodicWork(
            UPTIME_WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    override fun cameraWorker() {
        workManager().beginUniqueWork(
            CameraWorker::class.java.simpleName,
            ExistingWorkPolicy.KEEP,
            OneTimeWorkRequestBuilder<CameraWorker>().build()
        ).enqueue()
    }
}
