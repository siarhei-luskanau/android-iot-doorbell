package siarhei.luskanau.iot.doorbell.koin

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.work.Configuration
import androidx.work.WorkManager
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import siarhei.luskanau.iot.doorbell.BuildConfig
import siarhei.luskanau.iot.doorbell.data.AppBackgroundServices
import siarhei.luskanau.iot.doorbell.data.ScheduleWorkManagerService
import siarhei.luskanau.iot.doorbell.koin.di.appModule
import siarhei.luskanau.iot.doorbell.koin.di.getFragmentFactory
import siarhei.luskanau.iot.doorbell.koin.doorbelllist.di.doorbellListModule
import siarhei.luskanau.iot.doorbell.koin.imagedetails.di.imageDetailsModule
import siarhei.luskanau.iot.doorbell.koin.imagelist.di.imageListModule
import siarhei.luskanau.iot.doorbell.koin.permissions.di.permissionsModule
import siarhei.luskanau.iot.doorbell.koin.splash.di.splashModule
import siarhei.luskanau.iot.doorbell.navigation.OnActivityCreatedLifecycleCallbacks
import siarhei.luskanau.iot.doorbell.workmanager.DefaultWorkerFactory
import timber.log.Timber

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@AppApplication)
            androidFileProperties()
            modules(
                listOf(
                    appModule,
                    doorbellListModule,
                    imageDetailsModule,
                    imageListModule,
                    permissionsModule,
                    splashModule
                )
            )
        }

        val workerFactory = DefaultWorkerFactory(
            thisDeviceRepository = { get() },
            doorbellRepository = { get() },
            cameraRepository = { get() },
            uptimeRepository = { get() }
        )
        val config = Configuration.Builder().setWorkerFactory(workerFactory).build()
        WorkManager.initialize(this, config)

        get<ScheduleWorkManagerService>().startUptimeNotifications()
        get<AppBackgroundServices>().startServices()

        registerActivityLifecycleCallbacks(
            OnActivityCreatedLifecycleCallbacks {
                (it as? FragmentActivity?)?.let { fragmentActivity ->
                    fragmentActivity.supportFragmentManager.fragmentFactory =
                        getFragmentFactory(fragmentActivity)
                }
            }
        )
    }
}
