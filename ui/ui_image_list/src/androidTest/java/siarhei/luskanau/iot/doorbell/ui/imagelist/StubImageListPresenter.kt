package siarhei.luskanau.iot.doorbell.ui.imagelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.ImageData

open class StubImageListPresenter : ImageListPresenter {
    override val imageListStateFlow: Flow<ImageListState> = emptyFlow()
    override val loadingData: LiveData<Boolean> = MutableLiveData()
    override fun refreshData() = Unit
    override fun onCameraClicked(cameraData: CameraData) = Unit
    override fun onImageClicked(imageData: ImageData) = Unit
    override fun rebootDevice() = Unit
}
