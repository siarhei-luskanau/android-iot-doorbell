package siarhei.luskanau.iot.doorbell.work_manager

import androidx.work.*
import siarhei.luskanau.iot.doorbell.data.UptimeService
import siarhei.luskanau.iot.doorbell.work_manager.WorkManagerConstants.REPEAT_INTERVAL
import siarhei.luskanau.iot.doorbell.work_manager.WorkManagerConstants.REPEAT_INTERVAL_TIME_UNIT
import siarhei.luskanau.iot.doorbell.work_manager.WorkManagerConstants.UPTIME_WORK_NAME
import siarhei.luskanau.iot.doorbell.work_manager.camera.CameraWorker
import siarhei.luskanau.iot.doorbell.work_manager.uptime.UptimeWorker

class DefaultUptimeService(
        private val workManager: WorkManager
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

        workManager.enqueueUniquePeriodicWork(
                UPTIME_WORK_NAME,
                ExistingPeriodicWorkPolicy.REPLACE,
                request/**/
        )
    }

    override fun cameraWorker() {
        workManager.beginUniqueWork(
                CameraWorker::class.java.simpleName,
                ExistingWorkPolicy.KEEP,
                OneTimeWorkRequest.Builder(CameraWorker::class.java)
                        .build()
        ).enqueue()
    }

}