package siarhei.luskanau.iot.doorbell.dagger.doorbelllist

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import javax.inject.Provider
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import siarhei.luskanau.iot.doorbell.dagger.common.CommonComponent
import siarhei.luskanau.iot.doorbell.dagger.common.DaggerFragmentFactory
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListFragment
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListPresenterImpl
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListViewModel

@Module
class DoorbellListBuilderModule {

    @Provides
    fun providesFragmentFactory(
        providers: MutableMap<Class<out Fragment>, Provider<Fragment>>
    ): FragmentFactory = DaggerFragmentFactory(
        providers
    )

    @Provides
    fun provideDoorbellListViewModel(
        commonComponent: CommonComponent
    ) = DoorbellListViewModel(
        schedulerSet = commonComponent.provideSchedulerSet(),
        doorbellRepository = commonComponent.provideDoorbellRepository(),
        thisDeviceRepository = commonComponent.provideThisDeviceRepository(),
        cameraRepository = commonComponent.provideCameraRepository(),
        doorbellsDataSource = commonComponent.provideDoorbellsDataSource()
    )

    @Provides
    fun provideDoorbellListFragment(
        activity: FragmentActivity,
        viewModelFactory: ViewModelProvider.Factory,
        appNavigation: AppNavigation,
        commonComponent: CommonComponent
    ) = DoorbellListFragment {
        val placeListViewModel =
            ViewModelProvider(activity, viewModelFactory).get(DoorbellListViewModel::class.java)
        DoorbellListPresenterImpl(
            doorbellListViewModel = placeListViewModel,
            appNavigation = appNavigation,
            thisDeviceRepository = commonComponent.provideThisDeviceRepository()
        )
    }
}
