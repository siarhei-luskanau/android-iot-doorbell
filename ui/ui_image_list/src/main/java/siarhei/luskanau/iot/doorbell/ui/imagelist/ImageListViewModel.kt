package siarhei.luskanau.iot.doorbell.ui.imagelist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.Config
import androidx.paging.toLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import siarhei.luskanau.iot.doorbell.common.AppConstants
import siarhei.luskanau.iot.doorbell.common.ImagesDataSourceFactory
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeRepository

class ImageListViewModel(
    private val doorbellRepository: DoorbellRepository,
    private val imagesDataSourceFactory: ImagesDataSourceFactory,
    private val uptimeRepository: UptimeRepository
) : ViewModel() {

    private val deviceIdLiveData = MutableLiveData<String>()

    val imageListStateFlow: Flow<ImageListState> = deviceIdLiveData
        .asFlow()
        .flatMapLatest { deviceId: String ->
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
        .onStart {
            loadingData.postValue(true)
        }
        .onEach {
            loadingData.postValue(false)
        }

    val loadingData = MutableLiveData<Boolean>()

    fun requestData(deviceId: String) {
        deviceIdLiveData.postValue(deviceId)
    }

    fun onCameraClicked(doorbellData: DoorbellData, cameraData: CameraData) {
        viewModelScope.launch {
            doorbellRepository.sendCameraImageRequest(
                deviceId = doorbellData.doorbellId,
                cameraId = cameraData.cameraId,
                isRequested = true
            )
        }
    }

    fun rebootDevice(doorbellId: String, currentTime: Long) {
        viewModelScope.launch {
            uptimeRepository.uptimeRebootRequest(
                doorbellId = doorbellId,
                rebootRequestTimeMillis = currentTime,
                rebootRequestTimeString = AppConstants.DATE_FORMAT.format(currentTime)
            )
        }
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}
