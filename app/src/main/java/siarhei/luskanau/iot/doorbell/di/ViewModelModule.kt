package siarhei.luskanau.iot.doorbell.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import siarhei.luskanau.iot.doorbell.di.common.AppViewModelFactory
import siarhei.luskanau.iot.doorbell.di.common.ViewModelKey
import siarhei.luskanau.iot.doorbell.viewmodel.*

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
    @IntoMap
    @ViewModelKey(CamerasViewModel::class)
    internal abstract fun bindCamerasViewModel(viewModel: CamerasViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CameraImageRequestVewModel::class)
    internal abstract fun bindCameraImageRequestVewModel(viewModel: CameraImageRequestVewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RebootRequestViewModel::class)
    internal abstract fun bindRebootRequestViewModel(viewModel: RebootRequestViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory

}
