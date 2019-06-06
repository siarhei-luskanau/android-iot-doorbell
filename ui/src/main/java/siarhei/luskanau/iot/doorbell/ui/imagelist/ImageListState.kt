package siarhei.luskanau.iot.doorbell.ui.imagelist

import androidx.paging.PagedList
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.ImageData

sealed class ImageListState

data class EmptyImageListState(val cameraList: List<CameraData>) : ImageListState()

data class NormalImageListState(
    val cameraList: List<CameraData>,
    val imageList: PagedList<ImageData>?
) : ImageListState()

data class ErrorImageListState(val error: Throwable) : ImageListState()
