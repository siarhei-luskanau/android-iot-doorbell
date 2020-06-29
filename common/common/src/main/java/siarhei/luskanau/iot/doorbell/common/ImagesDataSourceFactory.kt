package siarhei.luskanau.iot.doorbell.common

import androidx.paging.Pager
import siarhei.luskanau.iot.doorbell.data.model.ImageData

interface ImagesDataSourceFactory {

    fun createPager(deviceId: String): Pager<String, ImageData>
}
