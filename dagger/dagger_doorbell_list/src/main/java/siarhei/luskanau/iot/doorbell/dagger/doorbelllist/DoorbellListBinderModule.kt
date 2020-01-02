package siarhei.luskanau.iot.doorbell.dagger.doorbelllist

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import siarhei.luskanau.iot.doorbell.dagger.common.DaggerViewModelFactory
import siarhei.luskanau.iot.doorbell.dagger.common.FragmentKey
import siarhei.luskanau.iot.doorbell.dagger.common.ViewModelKey
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListFragment
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListViewModel

@Module
interface DoorbellListBinderModule {

    @Binds
    fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DoorbellListViewModel::class)
    fun bindDoorbellListViewModel(viewModel: DoorbellListViewModel): ViewModel

    @Binds
    @IntoMap
    @FragmentKey(DoorbellListFragment::class)
    fun bindDoorbellListFragment(fragment: DoorbellListFragment): Fragment
}
