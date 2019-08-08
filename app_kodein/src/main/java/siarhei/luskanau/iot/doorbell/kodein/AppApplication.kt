package siarhei.luskanau.iot.doorbell.kodein

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.instance
import siarhei.luskanau.iot.doorbell.BuildConfig
import siarhei.luskanau.iot.doorbell.data.AppBackgroundServices
import siarhei.luskanau.iot.doorbell.data.ScheduleWorkManagerService
import siarhei.luskanau.iot.doorbell.data.repository.CameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeRepository
import siarhei.luskanau.iot.doorbell.kodein.di.activityModule
import siarhei.luskanau.iot.doorbell.kodein.di.appModule
import siarhei.luskanau.iot.doorbell.kodein.di.viewModelModule
import siarhei.luskanau.iot.doorbell.workmanager.DefaultWorkerFactory
import timber.log.Timber

class AppApplication : Application(), KodeinAware {

    private val thisDeviceRepository: ThisDeviceRepository by instance()
    private val doorbellRepository: DoorbellRepository by instance()
    private val cameraRepository: CameraRepository by instance()
    private val uptimeRepository: UptimeRepository by instance()
    private val scheduleWorkManagerService: ScheduleWorkManagerService by instance()
    private val appBackgroundServices: AppBackgroundServices by instance()

    override val kodein by Kodein.lazy {
        import(androidXModule(this@AppApplication))
        import(appModule)
        import(activityModule)
        import(viewModelModule)
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        val workerFactory = DefaultWorkerFactory(
            thisDeviceRepository = thisDeviceRepository,
            doorbellRepository = doorbellRepository,
            cameraRepository = cameraRepository,
            uptimeRepository = uptimeRepository
        )
        val config = Configuration.Builder().setWorkerFactory(workerFactory).build()
        WorkManager.initialize(this, config)

        scheduleWorkManagerService.startUptimeNotifications()
        appBackgroundServices.startServices()
    }
}
