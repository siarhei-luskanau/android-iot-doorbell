package siarhei.luskanau.iot.doorbell.di

import android.app.Application
import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import androidx.work.WorkerFactory
import siarhei.luskanau.iot.doorbell.data.AppBackgroundServices
import siarhei.luskanau.iot.doorbell.data.ScheduleWorkManagerService
import siarhei.luskanau.iot.doorbell.workmanager.DefaultWorkerFactory
import toothpick.Scope
import toothpick.ktp.KTP
import toothpick.ktp.extension.getInstance

class DiHolderImpl(context: Context) : DiHolder {

    private val scope: Scope by lazy {
        KTP.openScope(AppModule::class.java)
            .installModules(AppModule(context))
    }

    override fun onAppCreate(application: Application) {
        scope.getInstance<ScheduleWorkManagerService>().startUptimeNotifications()
        scope.getInstance<AppBackgroundServices>().startServices()
    }

    override fun onAppTrimMemory(application: Application) {
        scope.release()
    }

    override fun getFragmentFactory(fragmentActivity: FragmentActivity): FragmentFactory =
        ToothpickFragmentFactory(
            fragmentActivity = fragmentActivity,
            scope = scope
        )

    override fun provideWorkerFactory(): WorkerFactory =
        DefaultWorkerFactory(
            thisDeviceRepository = { scope.getInstance() },
            doorbellRepository = { scope.getInstance() },
            cameraRepository = { scope.getInstance() },
            uptimeRepository = { scope.getInstance() },
            imageRepository = { scope.getInstance() }
        )
}
