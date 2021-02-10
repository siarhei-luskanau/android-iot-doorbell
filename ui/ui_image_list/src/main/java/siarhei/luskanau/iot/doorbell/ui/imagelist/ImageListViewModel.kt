package siarhei.luskanau.iot.doorbell.ui.imagelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import siarhei.luskanau.iot.doorbell.common.ImagesDataSourceFactory
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository

class ImageListViewModel(
    private val doorbellId: String,
    private val appNavigation: AppNavigation,
    private val doorbellRepository: DoorbellRepository,
    imagesDataSourceFactory: ImagesDataSourceFactory,
) : ViewModel(), ImageListPresenter {

    override val viewStateFlow: Flow<ImageListState> =
        imagesDataSourceFactory.createPager(doorbellId)
            .flow
            .cachedIn(viewModelScope)
            .map { pagingData ->
                val cameraList = doorbellRepository.getCamerasList(doorbellId)
                ImageListState(
                    cameraList = cameraList,
                    pagingData = pagingData,
                )
            }

    override fun onCameraClicked(cameraData: CameraData) {
        viewModelScope.launch {
            doorbellRepository.sendCameraImageRequest(
                doorbellId = doorbellId,
                cameraId = cameraData.cameraId,
                isRequested = true
            )
        }
    }

    override fun onImageClicked(imageData: ImageData) {
        viewModelScope.launch {
            appNavigation.navigateToImageDetails(
                doorbellId = doorbellId,
                imageId = imageData.imageId
            )
        }
    }
}
