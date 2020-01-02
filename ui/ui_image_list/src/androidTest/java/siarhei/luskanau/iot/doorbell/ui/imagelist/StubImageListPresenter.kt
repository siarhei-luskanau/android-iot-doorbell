package siarhei.luskanau.iot.doorbell.ui.imagelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.ImageData

open class StubImageListPresenter : ImageListPresenter {

    override fun getImageListStateData(): LiveData<ImageListState> =
        MutableLiveData()

    override fun getLoadingData(): LiveData<Boolean> =
        MutableLiveData()

    override fun requestData() {}

    override fun onCameraClicked(cameraData: CameraData) {}

    override fun onImageClicked(imageData: ImageData) {}

    override fun rebootDevice() {}
}
