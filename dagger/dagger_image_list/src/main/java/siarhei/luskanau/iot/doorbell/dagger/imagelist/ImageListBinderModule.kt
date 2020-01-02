package siarhei.luskanau.iot.doorbell.dagger.imagelist

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import siarhei.luskanau.iot.doorbell.dagger.common.DaggerViewModelFactory
import siarhei.luskanau.iot.doorbell.dagger.common.FragmentKey
import siarhei.luskanau.iot.doorbell.dagger.common.ViewModelKey
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListFragment
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListViewModel

@Module
interface ImageListBinderModule {

    @Binds
    fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ImageListViewModel::class)
    fun bindImageListViewModel(viewModel: ImageListViewModel): ViewModel

    @Binds
    @IntoMap
    @FragmentKey(ImageListFragment::class)
    fun bindImageListFragment(fragment: ImageListFragment): Fragment
}
