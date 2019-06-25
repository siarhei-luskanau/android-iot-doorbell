package siarhei.luskanau.iot.doorbell.workmanager

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import siarhei.luskanau.iot.doorbell.data.UptimeService
import siarhei.luskanau.iot.doorbell.workmanager.WorkManagerConstants.REPEAT_INTERVAL
import siarhei.luskanau.iot.doorbell.workmanager.WorkManagerConstants.REPEAT_INTERVAL_TIME_UNIT
import siarhei.luskanau.iot.doorbell.workmanager.WorkManagerConstants.UPTIME_WORK_NAME
import javax.inject.Provider

class DefaultUptimeService(
    private val workManager: Provider<WorkManager>
) : UptimeService {

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

        workManager.get().enqueueUniquePeriodicWork(
                UPTIME_WORK_NAME,
                ExistingPeriodicWorkPolicy.REPLACE,
                request
        )
    }

    override fun cameraWorker() {
        workManager.get().beginUniqueWork(
                CameraWorker::class.java.simpleName,
                ExistingWorkPolicy.KEEP,
                OneTimeWorkRequest.Builder(CameraWorker::class.java)
                        .build()
        ).enqueue()
    }
}