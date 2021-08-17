package siarhei.luskanau.iot.doorbell.di

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListViewModel
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListFragmentArgs
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListViewModel
import timber.log.Timber

class AppViewModelFactory(
    private val appModules: AppModules,
    private val appNavigation: AppNavigation,
    private val args: Bundle?
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        Timber.d("AppViewModelFactory:instantiate:$modelClass")
        return when {

            DoorbellListViewModel::class.java.isAssignableFrom(modelClass) -> DoorbellListViewModel(
                appNavigation = appNavigation,
                thisDeviceRepository = appModules.thisDeviceRepository,
                doorbellsDataSource = appModules.doorbellsDataSource
            )

            ImageListViewModel::class.java.isAssignableFrom(modelClass) -> ImageListViewModel(
                doorbellId = ImageListFragmentArgs.fromBundle(requireNotNull(args)).doorbellId,
                appNavigation = appNavigation,
                doorbellRepository = appModules.doorbellRepository,
                imagesDataSourceFactory = appModules.imagesDataSourceFactory,
            )

            else -> modelClass.getConstructor().newInstance()
        } as T
    }
}
