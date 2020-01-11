package siarhei.luskanau.iot.doorbell.ui.imagelist

import androidx.lifecycle.LiveData
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.ImageData

class ImageListPresenterImpl(
    private val doorbellData: DoorbellData?,
    private val imageListViewModel: ImageListViewModel,
    private val appNavigation: AppNavigation
) : ImageListPresenter {

    override fun getImageListStateData(): LiveData<ImageListState> =
        imageListViewModel.imageListStateData

    override fun getLoadingData(): LiveData<Boolean> =
        imageListViewModel.loadingData

    override fun requestData() {
        doorbellData?.let { imageListViewModel.requestData(it.doorbellId) }
    }

    override fun onCameraClicked(cameraData: CameraData) {
        doorbellData?.let { imageListViewModel.onCameraClicked(it, cameraData) }
    }

    override fun onImageClicked(imageData: ImageData) {
        doorbellData?.let { appNavigation.navigateToImageDetails(it, imageData) }
    }

    override fun rebootDevice() {
        doorbellData?.let {
            imageListViewModel.rebootDevice(it.doorbellId, System.currentTimeMillis())
        }
    }
}