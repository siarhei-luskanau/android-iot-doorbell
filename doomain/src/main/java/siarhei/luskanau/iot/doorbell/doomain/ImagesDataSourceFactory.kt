package siarhei.luskanau.iot.doorbell.doomain

import androidx.paging.DataSource
import siarhei.luskanau.iot.doorbell.data.model.ImageData

interface ImagesDataSourceFactory {

    fun createDataSourceFactory(deviceId: String): DataSource.Factory<String, ImageData>
}
