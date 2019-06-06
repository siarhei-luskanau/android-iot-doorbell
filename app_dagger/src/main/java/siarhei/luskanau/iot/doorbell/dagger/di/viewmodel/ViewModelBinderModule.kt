package siarhei.luskanau.iot.doorbell.dagger.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import siarhei.luskanau.iot.doorbell.dagger.di.common.ViewModelKey
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListViewModel
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListViewModel

@Module
abstract class ViewModelBinderModule {

    @Binds
    abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DoorbellListViewModel::class)
    abstract fun bindDoorbellListViewModel(viewModel: DoorbellListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ImageListViewModel::class)
    abstract fun bindImageListViewModel(viewModel: ImageListViewModel): ViewModel
}
