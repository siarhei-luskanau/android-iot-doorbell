package siarhei.luskanau.iot.doorbell.kodein

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.work.WorkerFactory
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.direct
import org.kodein.di.instance
import siarhei.luskanau.iot.doorbell.data.AppBackgroundServices
import siarhei.luskanau.iot.doorbell.data.ScheduleWorkManagerService
import siarhei.luskanau.iot.doorbell.data.repository.CameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ImageRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeRepository
import siarhei.luskanau.iot.doorbell.kodein.di.activityModule
import siarhei.luskanau.iot.doorbell.kodein.di.appModule
import siarhei.luskanau.iot.doorbell.kodein.di.viewModelModule
import siarhei.luskanau.iot.doorbell.navigation.OnActivityCreatedLifecycleCallbacks
import siarhei.luskanau.iot.doorbell.workmanager.DefaultWorkerFactory
import siarhei.luskanau.iot.doorbell.workmanager.WorkerFactoryProvider
import timber.log.Timber

class AppApplication : Application(), WorkerFactoryProvider, DIAware {

    private val thisDeviceRepository: ThisDeviceRepository by instance()
    private val doorbellRepository: DoorbellRepository by instance()
    private val cameraRepository: CameraRepository by instance()
    private val uptimeRepository: UptimeRepository by instance()
    private val imageRepository: ImageRepository by instance()
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

    override fun provideWorkerFactory(): WorkerFactory =
        DefaultWorkerFactory(
            thisDeviceRepository = { thisDeviceRepository },
            doorbellRepository = { doorbellRepository },
            cameraRepository = { cameraRepository },
            uptimeRepository = { uptimeRepository },
            imageRepository = { imageRepository },
        )
}
