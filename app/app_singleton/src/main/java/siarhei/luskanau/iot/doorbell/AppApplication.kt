package siarhei.luskanau.iot.doorbell

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.work.WorkerFactory
import siarhei.luskanau.iot.doorbell.di.AppFragmentFactory
import siarhei.luskanau.iot.doorbell.di.AppModules
import siarhei.luskanau.iot.doorbell.navigation.OnActivityCreatedLifecycleCallbacks
import siarhei.luskanau.iot.doorbell.workmanager.DefaultWorkerFactory
import siarhei.luskanau.iot.doorbell.workmanager.WorkerFactoryProvider
import timber.log.Timber

class AppApplication : Application(), WorkerFactoryProvider {

    private val appModules: AppModules by lazy {
        AppModules(
            application = this
        )
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        appModules.scheduleWorkManagerService.startUptimeNotifications()
        appModules.appBackgroundServices.startServices()

        registerActivityLifecycleCallbacks(
            OnActivityCreatedLifecycleCallbacks {
                (it as? FragmentActivity?)?.let { fragmentActivity ->
                    fragmentActivity.supportFragmentManager.fragmentFactory = AppFragmentFactory(
                        fragmentActivity = fragmentActivity,
                        appModules = appModules
                    )
                }
            }
        )
    }

    override fun provideWorkerFactory(): WorkerFactory =
        DefaultWorkerFactory(
            thisDeviceRepository = { appModules.thisDeviceRepository },
            doorbellRepository = { appModules.doorbellRepository },
            cameraRepository = { appModules.cameraRepository },
            uptimeRepository = { appModules.uptimeRepository }
        )
}
