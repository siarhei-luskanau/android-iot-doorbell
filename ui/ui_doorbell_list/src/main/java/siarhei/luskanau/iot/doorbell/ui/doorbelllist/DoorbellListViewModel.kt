package siarhei.luskanau.iot.doorbell.ui.doorbelllist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Config
import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.paging.toFlowable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.coroutines.launch
import siarhei.luskanau.iot.doorbell.common.DoorbellsDataSource
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.repository.CameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
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
            runCatching {
                cameraList = cameraRepository.getCamerasList()
                updateLiveDate()
            }.onFailure {
                doorbellListStateData.postValue(ErrorDoorbellListState(it))
            }
        }

        createDataSourceFactory().toFlowable(
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

    fun onCameraClicked(cameraData: CameraData) {
        viewModelScope.launch(schedulerSet.ioCoroutineContext) {
            runCatching {
                doorbellRepository.sendCameraImageRequest(
                    deviceId = thisDeviceRepository.doorbellId(),
                    cameraId = cameraData.cameraId,
                    isRequested = true
                )
            }.onFailure {
                doorbellListStateData.postValue(ErrorDoorbellListState(it))
            }
        }
    }
}
