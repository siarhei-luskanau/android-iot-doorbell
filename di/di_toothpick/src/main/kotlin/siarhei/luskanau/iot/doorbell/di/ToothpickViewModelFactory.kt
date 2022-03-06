package siarhei.luskanau.iot.doorbell.di

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import siarhei.luskanau.iot.doorbell.common.DoorbellsDataSource
import siarhei.luskanau.iot.doorbell.common.ImagesDataSourceFactory
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import siarhei.luskanau.iot.doorbell.navigation.DefaultAppNavigation
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListViewModel
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListFragmentArgs
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListViewModel
import siarhei.luskanau.iot.doorbell.ui.splash.SplashViewModel
import timber.log.Timber
import toothpick.Scope

class ToothpickViewModelFactory(
    private val scope: Scope,
    private val appNavigation: DefaultAppNavigation,
    private val args: Bundle?
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        Timber.d("AppViewModelFactory:instantiate:$modelClass")
        return when {

            SplashViewModel::class.java.isAssignableFrom(modelClass) -> SplashViewModel(
                splashNavigation = appNavigation
            )

            DoorbellListViewModel::class.java.isAssignableFrom(modelClass) -> DoorbellListViewModel(
                appNavigation = appNavigation,
                thisDeviceRepository = scope.getInstance(ThisDeviceRepository::class.java),
                doorbellsDataSource = scope.getInstance(DoorbellsDataSource::class.java)
            )

            ImageListViewModel::class.java.isAssignableFrom(modelClass) -> ImageListViewModel(
                doorbellId = ImageListFragmentArgs.fromBundle(requireNotNull(args)).doorbellId,
                appNavigation = appNavigation,
                doorbellRepository = scope.getInstance(DoorbellRepository::class.java),
                imagesDataSourceFactory = scope.getInstance(ImagesDataSourceFactory::class.java),
            )

            else -> modelClass.getConstructor().newInstance()
        } as T
    }
}
