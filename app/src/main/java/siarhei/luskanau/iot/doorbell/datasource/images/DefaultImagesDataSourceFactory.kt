package siarhei.luskanau.iot.doorbell.datasource.images

import android.arch.paging.DataSource
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository

class DefaultImagesDataSourceFactory(
        private val schedulerSet: SchedulerSet,
        private val doorbellRepository: DoorbellRepository
) : ImagesDataSourceFactory {

    override fun createDataSourceFactory(deviceId: String) =
            object : DataSource.Factory<String, ImageData>() {
                override fun create(): DataSource<String, ImageData> =
                        ImagesDataSource(
                                schedulerSet,
                                doorbellRepository,
                                deviceId
                        )
            }

}