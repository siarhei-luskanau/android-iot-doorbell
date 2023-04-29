package siarhei.luskanau.iot.doorbell.ui.imagelist

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.ImageData

interface ImageListPresenter {
    suspend fun getCameraList(): List<CameraData>
    val doorbellListFlow: Flow<PagingData<ImageData>>
    fun onCameraClicked(cameraData: CameraData)
    fun onImageClicked(imageData: ImageData)
}
