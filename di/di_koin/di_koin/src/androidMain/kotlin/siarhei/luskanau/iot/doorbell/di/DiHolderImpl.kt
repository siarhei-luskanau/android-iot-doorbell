package siarhei.luskanau.iot.doorbell.di

import android.app.Application
import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import androidx.work.WorkerFactory
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.parameter.parametersOf
import siarhei.luskanau.iot.doorbell.data.AppBackgroundServices
import siarhei.luskanau.iot.doorbell.data.ScheduleWorkManagerService
import siarhei.luskanau.iot.doorbell.koin.doorbelllist.di.doorbellListModule
import siarhei.luskanau.iot.doorbell.koin.imagedetails.di.imageDetailsModule
import siarhei.luskanau.iot.doorbell.koin.imagelist.di.imageListModule
import siarhei.luskanau.iot.doorbell.koin.permissions.di.permissionsModule
import siarhei.luskanau.iot.doorbell.koin.splash.di.splashModule
import siarhei.luskanau.iot.doorbell.workmanager.DefaultWorkerFactory

class DiHolderImpl(context: Context) : DiHolder {

    private val koinApplication by lazy {
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(context)
            modules(
                listOf(
                    appModule,
                    doorbellListModule,
                    imageDetailsModule,
                    imageListModule,
                    permissionsModule,
                    splashModule,
                ),
            )
        }
    }

    override fun onAppCreate(application: Application) {
        koinApplication.koin.get<ScheduleWorkManagerService>().startUptimeNotifications()
        koinApplication.koin.get<AppBackgroundServices>().startServices()
    }

    override fun onAppTrimMemory(application: Application) = Unit

    override fun getFragmentFactory(fragmentActivity: FragmentActivity): FragmentFactory =
        koinApplication.koin.get { parametersOf(fragmentActivity) }

    override fun provideWorkerFactory(): WorkerFactory =
        DefaultWorkerFactory(
            thisDeviceRepository = { koinApplication.koin.get() },
            doorbellRepository = { koinApplication.koin.get() },
            imageSenderRepository = { koinApplication.koin.get() },
            cameraRepository = { koinApplication.koin.get() },
            uptimeRepository = { koinApplication.koin.get() },
        )
}
