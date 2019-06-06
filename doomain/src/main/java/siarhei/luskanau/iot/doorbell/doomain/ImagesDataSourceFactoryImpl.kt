package siarhei.luskanau.iot.doorbell.doomain

import androidx.paging.DataSource
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.data.repository.CachedRepository

class ImagesDataSourceFactoryImpl(
    private val cachedRepository: CachedRepository
) : ImagesDataSourceFactory {

    override fun createDataSourceFactory(deviceId: String) =
        object : DataSource.Factory<String, ImageData>() {
            override fun create(): DataSource<String, ImageData> =
                ImagesDataSourceImpl(
                    cachedRepository,
                    deviceId
                )
        }
}