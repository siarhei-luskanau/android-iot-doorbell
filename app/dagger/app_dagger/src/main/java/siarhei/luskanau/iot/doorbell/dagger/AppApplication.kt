package siarhei.luskanau.iot.doorbell.dagger

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.work.WorkerFactory
import siarhei.luskanau.iot.doorbell.dagger.common.CommonComponent
import siarhei.luskanau.iot.doorbell.dagger.common.DaggerCommonComponent
import siarhei.luskanau.iot.doorbell.dagger.doorbelllist.DaggerDoorbellListComponent
import siarhei.luskanau.iot.doorbell.dagger.imagedetails.DaggerImageDetailsComponent
import siarhei.luskanau.iot.doorbell.dagger.imagelist.DaggerImageListComponent
import siarhei.luskanau.iot.doorbell.dagger.permissions.DaggerPermissionsComponent
import siarhei.luskanau.iot.doorbell.dagger.splash.DaggerSplashComponent
import siarhei.luskanau.iot.doorbell.navigation.DefaultAppNavigation
import siarhei.luskanau.iot.doorbell.navigation.OnActivityCreatedLifecycleCallbacks
import siarhei.luskanau.iot.doorbell.workmanager.DefaultWorkerFactory
import siarhei.luskanau.iot.doorbell.workmanager.WorkerFactoryProvider
import timber.log.Timber

class AppApplication : Application(), WorkerFactoryProvider {

    private val commonComponent: CommonComponent by lazy {
        DaggerCommonComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        commonComponent.provideScheduleWorkManagerService().startUptimeNotifications()
        commonComponent.provideAppBackgroundServices().startServices()

        registerActivityLifecycleCallbacks(
            OnActivityCreatedLifecycleCallbacks {
                (it as? FragmentActivity?)?.let { fragmentActivity ->
                    val appNavigation = DefaultAppNavigation(fragmentActivity)

                    val fragmentFactory =
                        DelegateFragmentFactory(
                            listOf(
                                {
                                    DaggerSplashComponent.factory()
                                        .create(appNavigation)
                                        .provideFragmentFactory()
                                        .get()
                                },
                                {
                                    DaggerPermissionsComponent.factory()
                                        .create(appNavigation)
                                        .provideFragmentFactory()
                                        .get()
                                },
                                {
                                    DaggerDoorbellListComponent.factory()
                                        .create(
                                            appNavigation = appNavigation,
                                            commonComponent = commonComponent,
                                        )
                                        .provideFragmentFactory()
                                        .get()
                                },
                                {
                                    DaggerImageListComponent.factory()
                                        .create(
                                            appNavigation = appNavigation,
                                            commonComponent = commonComponent,
                                        )
                                        .provideFragmentFactory()
                                        .get()
                                },
                                {
                                    DaggerImageDetailsComponent.factory()
                                        .create(
                                            appNavigation = appNavigation,
                                            commonComponent = commonComponent,
                                        )
                                        .provideFragmentFactory()
                                        .get()
                                }
                            )
                        )

                    fragmentActivity.supportFragmentManager.fragmentFactory = fragmentFactory
                }
            }
        )
    }

    override fun provideWorkerFactory(): WorkerFactory =
        DefaultWorkerFactory(
            thisDeviceRepository = { commonComponent.provideThisDeviceRepository() },
            doorbellRepository = { commonComponent.provideDoorbellRepository() },
            cameraRepository = { commonComponent.provideCameraRepository() },
            uptimeRepository = { commonComponent.provideUptimeRepository() },
            imageRepository = { commonComponent.provideImageRepository() },
        )
}
