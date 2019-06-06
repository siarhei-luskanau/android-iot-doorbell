package siarhei.luskanau.iot.doorbell.dagger.di.viewmodel

import dagger.Module
import dagger.Provides
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.repository.CameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeRepository
import siarhei.luskanau.iot.doorbell.doomain.DoorbellsDataSource
import siarhei.luskanau.iot.doorbell.doomain.ImagesDataSourceFactory
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListViewModel
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListViewModel

@Module
class ViewModelBuilderModule {

    @Provides
    fun provideDoorbellListViewModel(
        schedulerSet: SchedulerSet,
        doorbellRepository: DoorbellRepository,
        thisDeviceRepository: ThisDeviceRepository,
        cameraRepository: CameraRepository,
        doorbellsDataSource: DoorbellsDataSource
    ) = DoorbellListViewModel(
        schedulerSet = schedulerSet,
        doorbellRepository = doorbellRepository,
        thisDeviceRepository = thisDeviceRepository,
        cameraRepository = cameraRepository,
        doorbellsDataSource = doorbellsDataSource
    )

    @Provides
    fun provideImageListViewModel(
        schedulerSet: SchedulerSet,
        doorbellRepository: DoorbellRepository,
        cameraRepository: CameraRepository,
        imagesDataSourceFactory: ImagesDataSourceFactory,
        uptimeRepository: UptimeRepository
    ) = ImageListViewModel(
        schedulerSet = schedulerSet,
        doorbellRepository = doorbellRepository,
        cameraRepository = cameraRepository,
        imagesDataSourceFactory = imagesDataSourceFactory,
        uptimeRepository = uptimeRepository
    )
}
