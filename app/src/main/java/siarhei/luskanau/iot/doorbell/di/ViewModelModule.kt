package siarhei.luskanau.iot.doorbell.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import siarhei.luskanau.iot.doorbell.di.common.AppViewModelFactory
import siarhei.luskanau.iot.doorbell.di.common.ViewModelKey
import siarhei.luskanau.iot.doorbell.ui.doorbells.DoorbellsViewModel
import siarhei.luskanau.iot.doorbell.ui.images.ImagesViewModel

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(DoorbellsViewModel::class)
    internal abstract fun bindDoorbellsViewModel(viewModel: DoorbellsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ImagesViewModel::class)
    internal abstract fun bindImagesViewModel(viewModel: ImagesViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory

}
