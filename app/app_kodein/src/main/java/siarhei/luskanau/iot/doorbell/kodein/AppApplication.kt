package siarhei.luskanau.iot.doorbell.kodein

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.work.Configuration
import androidx.work.WorkManager
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.direct
import org.kodein.di.instance
import siarhei.luskanau.iot.doorbell.data.AppBackgroundServices
import siarhei.luskanau.iot.doorbell.data.ScheduleWorkManagerService
import siarhei.luskanau.iot.doorbell.data.repository.CameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeRepository
import siarhei.luskanau.iot.doorbell.kodein.di.activityModule
import siarhei.luskanau.iot.doorbell.kodein.di.appModule
import siarhei.luskanau.iot.doorbell.kodein.di.viewModelModule
import siarhei.luskanau.iot.doorbell.navigation.OnActivityCreatedLifecycleCallbacks
import siarhei.luskanau.iot.doorbell.workmanager.DefaultWorkerFactory
import timber.log.Timber

class AppApplication : Application(), DIAware {

    private val thisDeviceRepository: ThisDeviceRepository by instance()
    private val doorbellRepository: DoorbellRepository by instance()
    private val cameraRepository: CameraRepository by instance()
    private val uptimeRepository: UptimeRepository by instance()
    private val scheduleWorkManagerService: ScheduleWorkManagerService by instance()
    private val appBackgroundServices: AppBackgroundServices by instance()

    override val di by DI.lazy {
        import(androidXModule(this@AppApplication))
        import(appModule)
        import(activityModule)
        import(viewModelModule)
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        val workerFactory = DefaultWorkerFactory(
            thisDeviceRepository = { thisDeviceRepository },
            doorbellRepository = { doorbellRepository },
            cameraRepository = { cameraRepository },
            uptimeRepository = { uptimeRepository }
        )
        val config = Configuration.Builder().setWorkerFactory(workerFactory).build()
        WorkManager.initialize(this, config)

        scheduleWorkManagerService.startUptimeNotifications()
        appBackgroundServices.startServices()

        registerActivityLifecycleCallbacks(
            OnActivityCreatedLifecycleCallbacks {
                (it as? FragmentActivity?)?.let { fragmentActivity ->
                    fragmentActivity.supportFragmentManager.fragmentFactory =
                        di.direct.instance(arg = fragmentActivity)
                }
            }
        )
    }
}
