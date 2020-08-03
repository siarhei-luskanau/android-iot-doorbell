package siarhei.luskanau.iot.doorbell.ui.imagelist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
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

    private var pagingDataFlow: Flow<PagingData<ImageData>>? = null

    private val requestLiveData = MutableLiveData(Any())

    override val imageListStateFlow: Flow<ImageListState> = requestLiveData.asFlow()
        .onStart { loadingData.postValue(true) }
        .flatMapLatest { getPagingDataFlow() }
        .map { pagingData ->
            delay(DELAY)
            val deviceId = requireNotNull(doorbellData?.doorbellId)
            val cameraList = doorbellRepository.getCamerasList(deviceId)
            val doorbellData = doorbellRepository.getDoorbell(deviceId)

            NormalImageListState(
                cameraList = cameraList,
                pagingData = pagingData,
                isAndroidThings = doorbellData?.isAndroidThings == true
            ) as ImageListState
        }
        .catch { cause: Throwable ->
            emit(ErrorImageListState(error = cause))
        }
        .onEach { loadingData.postValue(false) }

    private fun getPagingDataFlow(): Flow<PagingData<ImageData>> {
        if (pagingDataFlow == null) {
            pagingDataFlow = doorbellData?.let {
                imagesDataSourceFactory.createPager(it.doorbellId).flow
            }
        }
        return requireNotNull(pagingDataFlow)
    }

    override val loadingData = MutableLiveData<Boolean>()

    override fun refreshData() {
        viewModelScope.launch {
            pagingDataFlow = null
            requestLiveData.postValue(Any())
        }
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

    companion object {
        private const val DELAY = 1_000L
    }
}
