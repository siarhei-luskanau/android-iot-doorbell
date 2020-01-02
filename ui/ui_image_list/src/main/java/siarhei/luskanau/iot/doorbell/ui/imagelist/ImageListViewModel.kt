package siarhei.luskanau.iot.doorbell.ui.imagelist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.paging.toFlowable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.coroutines.launch
import siarhei.luskanau.iot.doorbell.common.AppConstants
import siarhei.luskanau.iot.doorbell.common.ImagesDataSourceFactory
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeRepository
import siarhei.luskanau.iot.doorbell.ui.common.BaseViewModel

private const val PAGE_SIZE = 20

class ImageListViewModel(
    private val schedulerSet: SchedulerSet,
    private val doorbellRepository: DoorbellRepository,
    private val imagesDataSourceFactory: ImagesDataSourceFactory,
    private val uptimeRepository: UptimeRepository
) : BaseViewModel() {

    val imageListStateData = MutableLiveData<ImageListState>()

    val loadingData = MutableLiveData<Boolean>()

    private var cameraList: List<CameraData> = emptyList()
    private var imagePagedList: PagedList<ImageData>? = null
    private var doorbellData: DoorbellData? = null

    fun requestData(deviceId: String) {
        disposables.clear()

        viewModelScope.launch(schedulerSet.ioCoroutineContext) {
            runCatching {
                doorbellData = doorbellRepository.getDoorbell(deviceId)
                updateLiveDate()
            }.onFailure {
                imageListStateData.postValue(ErrorImageListState(it))
            }
        }

        viewModelScope.launch(schedulerSet.ioCoroutineContext) {
            runCatching {
                cameraList = doorbellRepository.getCamerasList(deviceId)
                updateLiveDate()
            }.onFailure {
                imageListStateData.postValue(ErrorImageListState(it))
            }
        }

        imagesDataSourceFactory.createDataSourceFactory(deviceId).toFlowable(
            config = Config(
                pageSize = PAGE_SIZE,
                prefetchDistance = PAGE_SIZE,
                initialLoadSizeHint = PAGE_SIZE
            )
        )
            .doOnSubscribe { loadingData.postValue(true) }
            .doOnNext { loadingData.postValue(false) }
            .doOnTerminate { loadingData.postValue(false) }
            .subscribeBy(
                onNext = { pagedList ->
                    imagePagedList = pagedList
                    updateLiveDate()
                },
                onError = {
                    imageListStateData.postValue(ErrorImageListState(it))
                }
            )
            .disposeOnCleared()
    }

    private fun updateLiveDate() {
        imageListStateData.postValue(
            if (imagePagedList?.isNotEmpty() == true) {
                NormalImageListState(
                    cameraList = cameraList,
                    imageList = requireNotNull(imagePagedList),
                    isAndroidThings = doorbellData?.isAndroidThings == true
                )
            } else {
                EmptyImageListState(
                    cameraList = cameraList,
                    isAndroidThings = doorbellData?.isAndroidThings == true
                )
            }
        )
    }

    fun onCameraClicked(doorbellData: DoorbellData, cameraData: CameraData) {
        viewModelScope.launch(schedulerSet.ioCoroutineContext) {
            runCatching {
                doorbellRepository.sendCameraImageRequest(
                    deviceId = doorbellData.doorbellId,
                    cameraId = cameraData.cameraId,
                    isRequested = true
                )
            }.onFailure {
                imageListStateData.postValue(ErrorImageListState(it))
            }
        }
    }

    fun rebootDevice(doorbellId: String, currentTime: Long) {
        viewModelScope.launch(schedulerSet.ioCoroutineContext) {
            runCatching {
                uptimeRepository.uptimeRebootRequest(
                    doorbellId = doorbellId,
                    rebootRequestTimeMillis = currentTime,
                    rebootRequestTimeString = AppConstants.DATE_FORMAT.format(currentTime)
                )
            }.onFailure {
                imageListStateData.postValue(ErrorImageListState(it))
            }
        }
    }
}
