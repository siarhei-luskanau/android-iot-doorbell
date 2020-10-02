package siarhei.luskanau.iot.doorbell.ui.imagelist

import androidx.paging.PagingData
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.ImageData

data class ImageListState(
    val pagingData: PagingData<ImageData>,
    val cameraList: List<CameraData>,
    val isAndroidThings: Boolean
)
