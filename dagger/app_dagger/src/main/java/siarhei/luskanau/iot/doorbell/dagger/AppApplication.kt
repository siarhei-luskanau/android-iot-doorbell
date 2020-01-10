package siarhei.luskanau.iot.doorbell.dagger

import android.app.Application
import androidx.fragment.app.FragmentActivity
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
import siarhei.luskanau.iot.doorbell.navigation.OnActivityCreatedLifecycleCallbacks
import siarhei.luskanau.iot.doorbell.workmanager.DefaultWorkerFactory
import timber.log.Timber

class AppApplication : Application() {

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

        registerActivityLifecycleCallbacks(OnActivityCreatedLifecycleCallbacks {
            (it as? FragmentActivity?)?.let { fragmentActivity ->
                val appNavigation: AppNavigation = DefaultAppNavigation(fragmentActivity)

                val fragmentFactory =
                    DelegateFragmentFactory(
                        listOf(
                            {
                                DaggerPermissionsComponent.builder()
                                    .bindAppNavigation(appNavigation)
                                    .build()
                                    .provideFragmentFactory()
                                    .get()
                            },
                            {
                                DaggerDoorbellListComponent.builder()
                                    .bindAppNavigation(appNavigation)
                                    .bindCommonComponent(commonComponent)
                                    .build()
                                    .provideFragmentFactory()
                                    .get()
                            },
                            {
                                DaggerImageListComponent.builder()
                                    .bindAppNavigation(appNavigation)
                                    .bindCommonComponent(commonComponent)
                                    .build()
                                    .provideFragmentFactory()
                                    .get()
                            },
                            {
                                DaggerImageDetailsComponent.builder()
                                    .bindCommonComponent(commonComponent)
                                    .build()
                                    .provideFragmentFactory()
                                    .get()
                            }
                        )
                    )

                fragmentActivity.supportFragmentManager.fragmentFactory = fragmentFactory
            }
        })
    }
}
