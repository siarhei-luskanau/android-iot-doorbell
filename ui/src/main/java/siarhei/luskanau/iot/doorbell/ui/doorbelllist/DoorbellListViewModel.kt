package siarhei.luskanau.iot.doorbell.ui.doorbelllist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Config
import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.paging.toFlowable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.coroutines.launch
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.repository.CameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import siarhei.luskanau.iot.doorbell.doomain.DoorbellsDataSource
import siarhei.luskanau.iot.doorbell.ui.common.BaseViewModel

private const val PAGE_SIZE = 20

class DoorbellListViewModel(
    private val schedulerSet: SchedulerSet,
    private val doorbellRepository: DoorbellRepository,
    private val thisDeviceRepository: ThisDeviceRepository,
    private val cameraRepository: CameraRepository,
    private val doorbellsDataSource: DoorbellsDataSource
) : BaseViewModel() {

    val doorbellListStateData = MutableLiveData<DoorbellListState>()

    val loadingData = MutableLiveData<Boolean>()

    private var cameraList: List<CameraData> = emptyList()
    private var doorbellPagedList: PagedList<DoorbellData>? = null

    fun requestData() {
        disposables.clear()

        viewModelScope.launch(schedulerSet.ioCoroutineContext) {
            try {
                cameraList = cameraRepository.getCamerasList()
                updateLiveDate()
            } catch (error: Throwable) {
                doorbellListStateData.postValue(ErrorDoorbellListState(error))
            }
        }.cancelOnCleared()

        createDataSourceFactory().toFlowable(
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
                    doorbellPagedList = pagedList
                    updateLiveDate()
                },
                onError = {
                    doorbellListStateData.postValue(ErrorDoorbellListState(it))
                }
            )
            .disposeOnCleared()
    }

    private fun updateLiveDate() {
        doorbellListStateData.postValue(
            if (doorbellPagedList?.isNotEmpty() == true) {
                NormalDoorbellListState(cameraList, requireNotNull(doorbellPagedList))
            } else {
                EmptyDoorbellListState(cameraList)
            }
        )
    }

    private fun createDataSourceFactory(): DataSource.Factory<String, DoorbellData> =
        object : DataSource.Factory<String, DoorbellData>() {
            override fun create() = doorbellsDataSource
        }

    private fun createBoundaryCallback(): PagedList.BoundaryCallback<DoorbellData> =
        object : PagedList.BoundaryCallback<DoorbellData>() {
            override fun onItemAtFrontLoaded(itemAtFront: DoorbellData) {}
            override fun onItemAtEndLoaded(itemAtEnd: DoorbellData) {}
            override fun onZeroItemsLoaded() {}
        }

    fun onCameraClicked(cameraData: CameraData) {
        viewModelScope.launch(schedulerSet.ioCoroutineContext) {
            try {
                doorbellRepository.sendCameraImageRequest(
                    deviceId = thisDeviceRepository.doorbellId(),
                    cameraId = cameraData.cameraId,
                    isRequested = true
                )
            } catch (error: Throwable) {
                doorbellListStateData.postValue(ErrorDoorbellListState(error))
            }
        }.cancelOnCleared()
    }
}
