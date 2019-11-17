package siarhei.luskanau.iot.doorbell

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.repository.CameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeRepository
import siarhei.luskanau.iot.doorbell.doomain.DoorbellsDataSource
import siarhei.luskanau.iot.doorbell.doomain.ImagesDataSourceFactory
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListViewModel
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListViewModel
import timber.log.Timber

class AppViewModelFactory(
    private val doorbellsDataSource: DoorbellsDataSource,
    private val schedulerSet: SchedulerSet,
    private val doorbellRepository: DoorbellRepository,
    private val thisDeviceRepository: ThisDeviceRepository,
    private val cameraRepository: CameraRepository,
    private val imagesDataSourceFactory: ImagesDataSourceFactory,
    private val uptimeRepository: UptimeRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        Timber.d("AppViewModelFactory:instantiate:$modelClass")
        return when {

            DoorbellListViewModel::class.java.isAssignableFrom(modelClass) -> DoorbellListViewModel(
                schedulerSet = schedulerSet,
                doorbellRepository = doorbellRepository,
                thisDeviceRepository = thisDeviceRepository,
                cameraRepository = cameraRepository,
                doorbellsDataSource = doorbellsDataSource
            ) as T

            ImageListViewModel::class.java.isAssignableFrom(modelClass) -> ImageListViewModel(
                schedulerSet = schedulerSet,
                doorbellRepository = doorbellRepository,
                imagesDataSourceFactory = imagesDataSourceFactory,
                uptimeRepository = uptimeRepository
            ) as T

            else -> modelClass.getConstructor().newInstance()
        }
    }
}
