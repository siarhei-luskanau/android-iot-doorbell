package siarhei.luskanau.iot.doorbell.ui.imagelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import siarhei.luskanau.iot.doorbell.common.AppConstants
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import siarhei.luskanau.iot.doorbell.common.ImagesDataSourceFactory
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeRepository

class ImageListViewModel(
    private val doorbellData: DoorbellData?,
    private val appNavigation: AppNavigation,
    private val doorbellRepository: DoorbellRepository,
    imagesDataSourceFactory: ImagesDataSourceFactory,
    private val uptimeRepository: UptimeRepository
) : ViewModel(), ImageListPresenter {

    override val viewStateFlow: Flow<ImageListState> =
        imagesDataSourceFactory.createPager(doorbellData?.doorbellId.orEmpty())
            .flow
            .cachedIn(viewModelScope)
            .map { pagingData ->
                val deviceId = requireNotNull(doorbellData?.doorbellId)
                val cameraList = doorbellRepository.getCamerasList(deviceId)
                val doorbellData = doorbellRepository.getDoorbell(deviceId)

                ImageListState(
                    cameraList = cameraList,
                    pagingData = pagingData,
                    isAndroidThings = doorbellData?.isAndroidThings == true
                )
            }

    override fun onCameraClicked(cameraData: CameraData) {
        viewModelScope.launch {
            doorbellData?.let {
                doorbellRepository.sendCameraImageRequest(
                    deviceId = it.doorbellId,
                    cameraId = cameraData.cameraId,
                    isRequested = true
                )
            }
        }
    }

    override fun onImageClicked(imageData: ImageData) {
        viewModelScope.launch {
            doorbellData?.let { appNavigation.navigateToImageDetails(it, imageData) }
        }
    }

    override fun rebootDevice() {
        viewModelScope.launch {
            doorbellData?.let {
                val currentTime = System.currentTimeMillis()
                uptimeRepository.uptimeRebootRequest(
                    doorbellId = it.doorbellId,
                    rebootRequestTimeMillis = currentTime,
                    rebootRequestTimeString = AppConstants.DATE_FORMAT.format(currentTime)
                )
            }
        }
    }
}
