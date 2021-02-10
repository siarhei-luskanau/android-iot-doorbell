package siarhei.luskanau.iot.doorbell.ui.imagelist

import kotlinx.coroutines.flow.Flow
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.ImageData

interface ImageListPresenter {
    val viewStateFlow: Flow<ImageListState>
    fun onCameraClicked(cameraData: CameraData)
    fun onImageClicked(imageData: ImageData)
}
