package siarhei.luskanau.iot.doorbell

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import siarhei.luskanau.iot.doorbell.di.AppModules
import siarhei.luskanau.iot.doorbell.workmanager.DefaultWorkerFactory
import timber.log.Timber

class AppApplication : Application() {

    val appModules: AppModules by lazy {
        AppModules(
            this
        )
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        val workerFactory = DefaultWorkerFactory(
            thisDeviceRepository = appModules.thisDeviceRepository,
            doorbellRepository = appModules.doorbellRepository,
            cameraRepository = appModules.cameraRepository,
            uptimeRepository = appModules.uptimeRepository
        )
        val config = Configuration.Builder().setWorkerFactory(workerFactory).build()
        WorkManager.initialize(this, config)

        appModules.scheduleWorkManagerService.startUptimeNotifications()
        appModules.appBackgroundServices.startServices()
    }
}
