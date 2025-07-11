package siarhei.luskanau.iot.doorbell.dagger.doorbelllist

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import javax.inject.Provider
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import siarhei.luskanau.iot.doorbell.dagger.common.CommonComponent
import siarhei.luskanau.iot.doorbell.dagger.common.DaggerFragmentFactory
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListFragment
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
        appNavigation: AppNavigation,
        commonComponent: CommonComponent
    ) = DoorbellListViewModel(
        appNavigation = appNavigation,
        thisDeviceRepository = commonComponent.provideThisDeviceRepository(),
        doorbellsDataSource = commonComponent.provideDoorbellsDataSource()
    )

    @Provides
    fun provideDoorbellListFragment(viewModelFactory: ViewModelProvider.Factory) =
        DoorbellListFragment { fragment: Fragment ->
            ViewModelProvider(fragment, viewModelFactory)[DoorbellListViewModel::class.java]
        }
}
