package siarhei.luskanau.iot.doorbell.toothpick

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.work.Configuration
import androidx.work.WorkManager
import siarhei.luskanau.iot.doorbell.data.AppBackgroundServices
import siarhei.luskanau.iot.doorbell.data.ScheduleWorkManagerService
import siarhei.luskanau.iot.doorbell.navigation.OnActivityCreatedLifecycleCallbacks
import siarhei.luskanau.iot.doorbell.toothpick.di.AppModule
import siarhei.luskanau.iot.doorbell.toothpick.di.ToothpickFragmentFactory
import siarhei.luskanau.iot.doorbell.workmanager.DefaultWorkerFactory
import timber.log.Timber
import toothpick.Scope
import toothpick.ktp.KTP
import toothpick.ktp.extension.getInstance

class AppApplication : Application() {

    val scope: Scope by lazy {
        KTP.openScope(AppModule::class.java)
            .installModules(AppModule(this))
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        val workerFactory = DefaultWorkerFactory(
            thisDeviceRepository = { scope.getInstance() },
            doorbellRepository = { scope.getInstance() },
            cameraRepository = { scope.getInstance() },
            uptimeRepository = { scope.getInstance() }
        )
        val config = Configuration.Builder().setWorkerFactory(workerFactory).build()
        WorkManager.initialize(this, config)

        scope.getInstance<ScheduleWorkManagerService>().startUptimeNotifications()
        scope.getInstance<AppBackgroundServices>().startServices()

        registerActivityLifecycleCallbacks(
            OnActivityCreatedLifecycleCallbacks {
                (it as? FragmentActivity?)?.let { fragmentActivity ->
                    fragmentActivity.supportFragmentManager.fragmentFactory =
                        ToothpickFragmentFactory(
                            fragmentActivity = fragmentActivity,
                            scope = scope
                        )
                }
            }
        )
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        scope.release()
    }
}
