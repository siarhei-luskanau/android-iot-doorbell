package siarhei.luskanau.iot.doorbell

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.work.Configuration
import androidx.work.WorkManager
import siarhei.luskanau.iot.doorbell.di.AppFragmentFactory
import siarhei.luskanau.iot.doorbell.di.AppModules
import siarhei.luskanau.iot.doorbell.navigation.OnActivityCreatedLifecycleCallbacks
import siarhei.luskanau.iot.doorbell.workmanager.DefaultWorkerFactory
import timber.log.Timber

class AppApplication : Application() {

    private val appModules: AppModules by lazy {
        AppModules(
            application = this
        )
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        val workerFactory = DefaultWorkerFactory(
            thisDeviceRepository = { appModules.thisDeviceRepository },
            doorbellRepository = { appModules.doorbellRepository },
            cameraRepository = { appModules.cameraRepository },
            uptimeRepository = { appModules.uptimeRepository }
        )
        val config = Configuration.Builder().setWorkerFactory(workerFactory).build()
        WorkManager.initialize(this, config)

        appModules.scheduleWorkManagerService.startUptimeNotifications()
        appModules.appBackgroundServices.startServices()

        registerActivityLifecycleCallbacks(OnActivityCreatedLifecycleCallbacks {
            (it as? FragmentActivity?)?.let { fragmentActivity ->
                fragmentActivity.supportFragmentManager.fragmentFactory = AppFragmentFactory(
                    fragmentActivity = fragmentActivity,
                    appModules = appModules
                )
            }
        })
    }
}
