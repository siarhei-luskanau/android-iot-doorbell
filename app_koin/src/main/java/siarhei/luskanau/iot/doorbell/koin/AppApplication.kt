package siarhei.luskanau.iot.doorbell.koin

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import siarhei.luskanau.iot.doorbell.BuildConfig
import siarhei.luskanau.iot.doorbell.data.AppBackgroundServices
import siarhei.luskanau.iot.doorbell.data.ScheduleWorkManagerService
import siarhei.luskanau.iot.doorbell.koin.di.activityModule
import siarhei.luskanau.iot.doorbell.koin.di.appModule
import siarhei.luskanau.iot.doorbell.koin.di.viewModelModule
import siarhei.luskanau.iot.doorbell.workmanager.DefaultWorkerFactory
import timber.log.Timber

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@AppApplication)
            androidFileProperties()
            modules(
                listOf(
                    appModule,
                    activityModule,
                    viewModelModule
                )
            )
        }

        val workerFactory = DefaultWorkerFactory(
            thisDeviceRepository = get(),
            doorbellRepository = get(),
            cameraRepository = get(),
            uptimeRepository = get()
        )
        val config = Configuration.Builder().setWorkerFactory(workerFactory).build()
        WorkManager.initialize(this, config)

        inject<ScheduleWorkManagerService>().value.startUptimeNotifications()
        inject<AppBackgroundServices>().value.startServices()
    }
}
