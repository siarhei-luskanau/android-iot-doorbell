package siarhei.luskanau.iot.doorbell.ui.imagelist

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.ImageData

interface ImageListPresenter {
    val imageListStateFlow: Flow<ImageListState>
    val loadingData: LiveData<Boolean>
    fun refreshData()
    fun onCameraClicked(cameraData: CameraData)
    fun onImageClicked(imageData: ImageData)
    fun rebootDevice()
}
