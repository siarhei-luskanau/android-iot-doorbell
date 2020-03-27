package siarhei.luskanau.iot.doorbell.toothpick.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import siarhei.luskanau.iot.doorbell.common.DoorbellsDataSource
import siarhei.luskanau.iot.doorbell.common.ImagesDataSourceFactory
import siarhei.luskanau.iot.doorbell.data.repository.CameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeRepository
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListViewModel
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListViewModel
import timber.log.Timber
import toothpick.Scope

class ToothpickViewModelFactory(
    private val scope: Scope
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        Timber.d("AppViewModelFactory:instantiate:$modelClass")
        return when {

            DoorbellListViewModel::class.java.isAssignableFrom(modelClass) -> DoorbellListViewModel(
                doorbellRepository = scope.getInstance(DoorbellRepository::class.java),
                thisDeviceRepository = scope.getInstance(ThisDeviceRepository::class.java),
                cameraRepository = scope.getInstance(CameraRepository::class.java),
                doorbellsDataSource = scope.getInstance(DoorbellsDataSource::class.java)
            )

            ImageListViewModel::class.java.isAssignableFrom(modelClass) -> ImageListViewModel(
                doorbellRepository = scope.getInstance(DoorbellRepository::class.java),
                imagesDataSourceFactory = scope.getInstance(ImagesDataSourceFactory::class.java),
                uptimeRepository = scope.getInstance(UptimeRepository::class.java)
            )

            else -> modelClass.getConstructor().newInstance()
        } as T
    }
}
