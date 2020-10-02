package siarhei.luskanau.iot.doorbell.ui.imagelist

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.ImageData

open class StubImageListPresenter : ImageListPresenter {
    override val viewStateFlow: Flow<ImageListState> = emptyFlow()
    override fun onCameraClicked(cameraData: CameraData) = Unit
    override fun onImageClicked(imageData: ImageData) = Unit
    override fun rebootDevice() = Unit
}
