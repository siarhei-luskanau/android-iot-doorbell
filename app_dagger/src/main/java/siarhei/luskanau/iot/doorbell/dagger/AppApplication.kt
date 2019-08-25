package siarhei.luskanau.iot.doorbell.dagger

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject
import siarhei.luskanau.iot.doorbell.BuildConfig
import siarhei.luskanau.iot.doorbell.dagger.di.AppComponent
import siarhei.luskanau.iot.doorbell.dagger.di.DaggerAppComponent
import siarhei.luskanau.iot.doorbell.data.AppBackgroundServices
import siarhei.luskanau.iot.doorbell.data.ScheduleWorkManagerService
import siarhei.luskanau.iot.doorbell.data.repository.CameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeRepository
import siarhei.luskanau.iot.doorbell.workmanager.DefaultWorkerFactory
import timber.log.Timber

class AppApplication : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>
    @Inject
    lateinit var thisDeviceRepository: ThisDeviceRepository
    @Inject
    lateinit var doorbellRepository: DoorbellRepository
    @Inject
    lateinit var cameraRepository: CameraRepository
    @Inject
    lateinit var uptimeRepository: UptimeRepository
    @Inject
    lateinit var scheduleWorkManagerService: ScheduleWorkManagerService
    @Inject
    lateinit var appBackgroundServices: AppBackgroundServices

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        appComponent = DaggerAppComponent
            .builder()
            .application(this)
            .build()

        appComponent.inject(this)

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

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector
}
