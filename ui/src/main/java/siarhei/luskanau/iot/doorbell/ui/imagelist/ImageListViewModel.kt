package siarhei.luskanau.iot.doorbell.ui.imagelist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.paging.toFlowable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.coroutines.launch
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeRepository
import siarhei.luskanau.iot.doorbell.doomain.AppConstants
import siarhei.luskanau.iot.doorbell.doomain.ImagesDataSourceFactory
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
            try {
                doorbellData = doorbellRepository.getDoorbell(deviceId)
                updateLiveDate()
            } catch (error: Throwable) {
                imageListStateData.postValue(ErrorImageListState(error))
            }
        }.cancelOnCleared()

        viewModelScope.launch(schedulerSet.ioCoroutineContext) {
            try {
                cameraList = doorbellRepository.getCamerasList(deviceId)
                updateLiveDate()
            } catch (error: Throwable) {
                imageListStateData.postValue(ErrorImageListState(error))
            }
        }.cancelOnCleared()

        imagesDataSourceFactory.createDataSourceFactory(deviceId).toFlowable(
            config = Config(
                pageSize = PAGE_SIZE,
                prefetchDistance = PAGE_SIZE,
                initialLoadSizeHint = PAGE_SIZE
            ),
            boundaryCallback = createBoundaryCallback()
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

    private fun createBoundaryCallback(): PagedList.BoundaryCallback<ImageData> =
        object : PagedList.BoundaryCallback<ImageData>() {
            override fun onItemAtFrontLoaded(itemAtFront: ImageData) {}
            override fun onItemAtEndLoaded(itemAtEnd: ImageData) {}
            override fun onZeroItemsLoaded() {}
        }

    fun onCameraClicked(doorbellData: DoorbellData, cameraData: CameraData) {
        viewModelScope.launch(schedulerSet.ioCoroutineContext) {
            try {
                doorbellRepository.sendCameraImageRequest(
                    deviceId = doorbellData.doorbellId,
                    cameraId = cameraData.cameraId,
                    isRequested = true
                )
            } catch (error: Throwable) {
                imageListStateData.postValue(ErrorImageListState(error))
            }
        }.cancelOnCleared()
    }

    fun rebootDevice(doorbellId: String, currentTime: Long) {
        viewModelScope.launch(schedulerSet.ioCoroutineContext) {
            try {
                uptimeRepository.uptimeRebootRequest(
                    doorbellId = doorbellId,
                    rebootRequestTimeMillis = currentTime,
                    rebootRequestTimeString = AppConstants.DATE_FORMAT.format(currentTime)
                )
            } catch (error: Throwable) {
                imageListStateData.postValue(ErrorImageListState(error))
            }
        }.cancelOnCleared()
    }
}
