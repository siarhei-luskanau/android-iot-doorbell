package siarhei.luskanau.iot.doorbell.common

import androidx.paging.ItemKeyedDataSource
import siarhei.luskanau.iot.doorbell.data.model.ImageData

abstract class ImagesDataSource : ItemKeyedDataSource<String, ImageData>()
