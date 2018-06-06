package siarhei.luskanau.iot.doorbell.datasource.images

import android.arch.paging.DataSource
import siarhei.luskanau.iot.doorbell.data.model.ImageData

interface ImagesDataSourceFactory {

    fun createDataSourceFactory(deviceId: String): DataSource.Factory<String, ImageData>

}