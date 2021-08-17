package siarhei.luskanau.iot.doorbell.koin

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.work.WorkerFactory
import org.koin.android.ext.android.get
import siarhei.luskanau.iot.doorbell.data.AppBackgroundServices
import siarhei.luskanau.iot.doorbell.data.ScheduleWorkManagerService
import siarhei.luskanau.iot.doorbell.koin.di.getFragmentFactory
import siarhei.luskanau.iot.doorbell.navigation.OnActivityCreatedLifecycleCallbacks
import siarhei.luskanau.iot.doorbell.workmanager.DefaultWorkerFactory
import siarhei.luskanau.iot.doorbell.workmanager.WorkerFactoryProvider
import timber.log.Timber

class AppApplication : Application(), WorkerFactoryProvider {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

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

    override fun provideWorkerFactory(): WorkerFactory =
        DefaultWorkerFactory(
            thisDeviceRepository = { get() },
            doorbellRepository = { get() },
            cameraRepository = { get() },
            uptimeRepository = { get() },
            imageRepository = { get() },
        )
}
