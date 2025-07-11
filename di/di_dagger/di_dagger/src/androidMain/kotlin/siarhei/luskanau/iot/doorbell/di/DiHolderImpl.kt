package siarhei.luskanau.iot.doorbell.di

import android.app.Application
import android.content.Context
import androidx.work.WorkerFactory
import siarhei.luskanau.iot.doorbell.dagger.common.CommonComponent
import siarhei.luskanau.iot.doorbell.dagger.common.DaggerCommonComponent
import siarhei.luskanau.iot.doorbell.workmanager.DefaultWorkerFactory

class DiHolderImpl(context: Context) : DiHolder {

    private val commonComponent: CommonComponent by lazy {
        DaggerCommonComponent.factory().create(context.applicationContext as Application)
    }

    override fun onAppCreate(application: Application) {
        commonComponent.provideScheduleWorkManagerService().startUptimeNotifications()
        commonComponent.provideAppBackgroundServices().startServices()
    }

    override fun onAppTrimMemory(application: Application) = Unit

    override fun provideWorkerFactory(): WorkerFactory = DefaultWorkerFactory(
        thisDeviceRepository = { commonComponent.provideThisDeviceRepository() },
        doorbellRepository = { commonComponent.provideDoorbellRepository() },
        imageSenderRepository = { commonComponent.provideImageSenderRepository() },
        cameraRepository = { commonComponent.provideCameraRepository() },
        uptimeRepository = { commonComponent.provideUptimeRepository() }
    )
}
