package siarhei.luskanau.iot.doorbell.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListViewModel
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListViewModel
import timber.log.Timber

class AppViewModelFactory(
    private val appModules: AppModules
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        Timber.d("AppViewModelFactory:instantiate:$modelClass")
        return when {

            DoorbellListViewModel::class.java.isAssignableFrom(modelClass) -> DoorbellListViewModel(
                schedulerSet = appModules.schedulerSet,
                doorbellRepository = appModules.doorbellRepository,
                thisDeviceRepository = appModules.thisDeviceRepository,
                cameraRepository = appModules.cameraRepository,
                doorbellsDataSource = appModules.doorbellsDataSource
            ) as T

            ImageListViewModel::class.java.isAssignableFrom(modelClass) -> ImageListViewModel(
                schedulerSet = appModules.schedulerSet,
                doorbellRepository = appModules.doorbellRepository,
                imagesDataSourceFactory = appModules.imagesDataSourceFactory,
                uptimeRepository = appModules.uptimeRepository
            ) as T

            else -> modelClass.getConstructor().newInstance()
        }
    }
}
