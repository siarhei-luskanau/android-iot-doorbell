package siarhei.luskanau.iot.doorbell.common

import androidx.paging.Pager
import siarhei.luskanau.iot.doorbell.data.model.ImageData

interface ImagesDataSourceFactory {

    fun createPager(doorbellId: String): Pager<String, ImageData>
}
