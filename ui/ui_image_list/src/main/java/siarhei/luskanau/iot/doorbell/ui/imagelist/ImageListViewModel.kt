package siarhei.luskanau.iot.doorbell.ui.imagelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.Config
import androidx.paging.toLiveData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
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
    private val imagesDataSourceFactory: ImagesDataSourceFactory,
    private val uptimeRepository: UptimeRepository
) : ViewModel(), ImageListPresenter {

    private val deviceIdLiveData = MutableLiveData<String>()

    override fun getImageListStateFlow(): Flow<ImageListState> = deviceIdLiveData
        .asFlow()
        .onEach { loadingData.postValue(true) }
        .flatMapLatest { deviceId: String ->
            delay(1_000L)

            imagesDataSourceFactory.createDataSourceFactory(deviceId).toLiveData(
                config = Config(
                    pageSize = PAGE_SIZE,
                    prefetchDistance = PAGE_SIZE,
                    initialLoadSizeHint = PAGE_SIZE
                )
            )
                .asFlow()
                .map { pagedList ->
                    val cameraList = doorbellRepository.getCamerasList(deviceId)

                    val doorbellData = doorbellRepository.getDoorbell(deviceId)

                    val state = if (pagedList.isNotEmpty()) {
                        NormalImageListState(
                            cameraList = cameraList,
                            imageList = pagedList,
                            isAndroidThings = doorbellData?.isAndroidThings == true
                        )
                    } else {
                        EmptyImageListState(
                            cameraList = cameraList,
                            isAndroidThings = doorbellData?.isAndroidThings == true
                        )
                    }
                    state
                }
                .catch { cause: Throwable ->
                    emit(ErrorImageListState(error = cause))
                }
        }
        .onEach { loadingData.postValue(false) }

    private val loadingData = MutableLiveData<Boolean>()
    override fun getLoadingData(): LiveData<Boolean> = loadingData

    override fun requestData() {
        doorbellData?.let { deviceIdLiveData.postValue(it.doorbellId) }
    }

    override fun onCameraClicked(cameraData: CameraData) {
        doorbellData?.let {
            viewModelScope.launch {
                doorbellRepository.sendCameraImageRequest(
                    deviceId = it.doorbellId,
                    cameraId = cameraData.cameraId,
                    isRequested = true
                )
            }
        }
    }

    override fun onImageClicked(imageData: ImageData) {
        doorbellData?.let { appNavigation.navigateToImageDetails(it, imageData) }
    }

    override fun rebootDevice() {
        doorbellData?.let {
            viewModelScope.launch {
                val currentTime = System.currentTimeMillis()
                uptimeRepository.uptimeRebootRequest(
                    doorbellId = it.doorbellId,
                    rebootRequestTimeMillis = currentTime,
                    rebootRequestTimeString = AppConstants.DATE_FORMAT.format(currentTime)
                )
            }
        }
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}
