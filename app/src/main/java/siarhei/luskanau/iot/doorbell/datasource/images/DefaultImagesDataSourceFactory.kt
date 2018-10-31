package siarhei.luskanau.iot.doorbell.datasource.images

import androidx.paging.DataSource
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.data.repository.CachedRepository

class DefaultImagesDataSourceFactory(
    private val cachedRepository: CachedRepository
) : ImagesDataSourceFactory {

    override fun createDataSourceFactory(deviceId: String) =
            object : DataSource.Factory<String, ImageData>() {
                override fun create(): DataSource<String, ImageData> =
                        ImagesDataSource(
                                cachedRepository,
                                deviceId
                        )
            }
}