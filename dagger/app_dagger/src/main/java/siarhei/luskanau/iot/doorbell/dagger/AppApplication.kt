package siarhei.luskanau.iot.doorbell.dagger

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import siarhei.luskanau.iot.doorbell.BuildConfig
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import siarhei.luskanau.iot.doorbell.dagger.common.CommonComponent
import siarhei.luskanau.iot.doorbell.dagger.common.DaggerCommonComponent
import siarhei.luskanau.iot.doorbell.dagger.doorbelllist.DaggerDoorbellListComponent
import siarhei.luskanau.iot.doorbell.dagger.imagedetails.DaggerImageDetailsComponent
import siarhei.luskanau.iot.doorbell.dagger.imagelist.DaggerImageListComponent
import siarhei.luskanau.iot.doorbell.dagger.permissions.DaggerPermissionsComponent
import siarhei.luskanau.iot.doorbell.navigation.DefaultAppNavigation
import siarhei.luskanau.iot.doorbell.navigation.DefaultAppNavigationArgs
import siarhei.luskanau.iot.doorbell.workmanager.DefaultWorkerFactory
import timber.log.Timber

class AppApplication : Application(), HasFragmentFactory {

    private val commonComponent: CommonComponent by lazy {
        DaggerCommonComponent
            .builder()
            .bindApplication(this)
            .bindAppNavigationArgs(DefaultAppNavigationArgs())
            .build()
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        val workerFactory = DefaultWorkerFactory(
            thisDeviceRepository = { commonComponent.provideThisDeviceRepository() },
            doorbellRepository = { commonComponent.provideDoorbellRepository() },
            cameraRepository = { commonComponent.provideCameraRepository() },
            uptimeRepository = { commonComponent.provideUptimeRepository() }
        )
        val config = Configuration.Builder().setWorkerFactory(workerFactory).build()
        WorkManager.initialize(this, config)

        commonComponent.provideScheduleWorkManagerService().startUptimeNotifications()
        commonComponent.provideAppBackgroundServices().startServices()
    }

    override fun buildFragmentFactory(fragmentActivity: FragmentActivity): FragmentFactory {
        val appNavigation: AppNavigation = DefaultAppNavigation(fragmentActivity)

        return DelegateFragmentFactory(
            listOf(

                DaggerPermissionsComponent.builder()
                    .bindAppNavigation(appNavigation)
                    .build()
                    .provideFragmentFactory(),

                DaggerDoorbellListComponent.builder()
                    .bindAppNavigation(appNavigation)
                    .bindCommonComponent(commonComponent)
                    .build()
                    .provideFragmentFactory(),

                DaggerImageListComponent.builder()
                    .bindAppNavigation(appNavigation)
                    .bindCommonComponent(commonComponent)
                    .build()
                    .provideFragmentFactory(),

                DaggerImageDetailsComponent.builder()
                    .bindCommonComponent(commonComponent)
                    .build()
                    .provideFragmentFactory()
            )
        )
    }
}
