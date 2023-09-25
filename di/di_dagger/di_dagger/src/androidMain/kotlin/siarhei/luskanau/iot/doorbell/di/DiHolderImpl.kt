package siarhei.luskanau.iot.doorbell.di

import android.app.Application
import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import androidx.work.WorkerFactory
import siarhei.luskanau.iot.doorbell.dagger.common.CommonComponent
import siarhei.luskanau.iot.doorbell.dagger.common.DaggerCommonComponent
import siarhei.luskanau.iot.doorbell.dagger.doorbelllist.DaggerDoorbellListComponent
import siarhei.luskanau.iot.doorbell.dagger.imagedetails.DaggerImageDetailsComponent
import siarhei.luskanau.iot.doorbell.dagger.imagelist.DaggerImageListComponent
import siarhei.luskanau.iot.doorbell.dagger.permissions.DaggerPermissionsComponent
import siarhei.luskanau.iot.doorbell.dagger.splash.DaggerSplashComponent
import siarhei.luskanau.iot.doorbell.navigation.DefaultAppNavigation
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

    override fun getFragmentFactory(fragmentActivity: FragmentActivity): FragmentFactory =
        DefaultAppNavigation(fragmentActivity).let { appNavigation ->
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
                    },
                ),
            )
        }

    override fun provideWorkerFactory(): WorkerFactory =
        DefaultWorkerFactory(
            thisDeviceRepository = { commonComponent.provideThisDeviceRepository() },
            doorbellRepository = { commonComponent.provideDoorbellRepository() },
            imageSenderRepository = { commonComponent.provideImageSenderRepository() },
            cameraRepository = { commonComponent.provideCameraRepository() },
            uptimeRepository = { commonComponent.provideUptimeRepository() },
        )
}
