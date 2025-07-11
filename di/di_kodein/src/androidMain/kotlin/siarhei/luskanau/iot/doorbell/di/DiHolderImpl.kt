package siarhei.luskanau.iot.doorbell.di

import android.app.Application
import android.content.Context
import androidx.work.WorkerFactory
import org.kodein.di.DI
import org.kodein.di.android.androidCoreModule
import org.kodein.di.direct
import org.kodein.di.instance
import siarhei.luskanau.iot.doorbell.data.AppBackgroundServices
import siarhei.luskanau.iot.doorbell.data.ScheduleWorkManagerService
import siarhei.luskanau.iot.doorbell.workmanager.DefaultWorkerFactory

class DiHolderImpl(context: Context) : DiHolder {

    private val di by DI.lazy {
        import(androidCoreModule(context.applicationContext as Application))
        import(appModule)
        import(activityModule)
        import(viewModelModule)
    }

    private val scheduleWorkManagerService: ScheduleWorkManagerService by di.instance()
    private val appBackgroundServices: AppBackgroundServices by di.instance()

    override fun onAppCreate(application: Application) {
        scheduleWorkManagerService.startUptimeNotifications()
        appBackgroundServices.startServices()
    }

    override fun onAppTrimMemory(application: Application) = Unit

    override fun provideWorkerFactory(): WorkerFactory = DefaultWorkerFactory(
        thisDeviceRepository = { di.direct.instance() },
        doorbellRepository = { di.direct.instance() },
        imageSenderRepository = { di.direct.instance() },
        cameraRepository = { di.direct.instance() },
        uptimeRepository = { di.direct.instance() }
    )
}
