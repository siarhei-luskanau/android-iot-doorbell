package siarhei.luskanau.iot.doorbell.common

import androidx.paging.PagingSource
import siarhei.luskanau.iot.doorbell.data.model.ImageData

abstract class ImagesDataSource : PagingSource<String, ImageData>()
