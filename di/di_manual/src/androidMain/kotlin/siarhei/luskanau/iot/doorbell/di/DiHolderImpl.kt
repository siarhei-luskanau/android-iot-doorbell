package siarhei.luskanau.iot.doorbell.di

import android.app.Application
import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import androidx.work.WorkerFactory
import siarhei.luskanau.iot.doorbell.workmanager.DefaultWorkerFactory

class DiHolderImpl(context: Context) : DiHolder {

    private val appModules by lazy {
        AppModules(context = context)
    }

    override fun onAppCreate(application: Application) {
        appModules.scheduleWorkManagerService.startUptimeNotifications()
        appModules.appBackgroundServices.startServices()
    }

    override fun onAppTrimMemory(application: Application) = Unit

    override fun getFragmentFactory(fragmentActivity: FragmentActivity): FragmentFactory =
        AppFragmentFactory(
            fragmentActivity = fragmentActivity,
            appModules = appModules,
        )

    override fun provideWorkerFactory(): WorkerFactory =
        DefaultWorkerFactory(
            thisDeviceRepository = { appModules.thisDeviceRepository },
            doorbellRepository = { appModules.doorbellRepository },
            imageSenderRepository = { appModules.imageSenderRepository },
            cameraRepository = { appModules.cameraRepository },
            uptimeRepository = { appModules.uptimeRepository },
        )
}
