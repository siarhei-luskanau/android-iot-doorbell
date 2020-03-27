package siarhei.luskanau.iot.doorbell.ui.doorbelllist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.Config
import androidx.paging.DataSource
import androidx.paging.toLiveData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import siarhei.luskanau.iot.doorbell.common.DoorbellsDataSource
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.repository.CameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository

@ExperimentalCoroutinesApi
class DoorbellListViewModel(
    private val doorbellRepository: DoorbellRepository,
    private val thisDeviceRepository: ThisDeviceRepository,
    private val cameraRepository: CameraRepository,
    private val doorbellsDataSource: DoorbellsDataSource
) : ViewModel() {

    val doorbellListStateFlow: Flow<DoorbellListState> =
        createDataSourceFactory()
            .toLiveData(
                config = Config(
                    pageSize = PAGE_SIZE,
                    prefetchDistance = PAGE_SIZE,
                    initialLoadSizeHint = PAGE_SIZE
                )
            )
            .asFlow()
            .map { pagedList ->
                val cameraList = cameraRepository.getCamerasList()
                if (pagedList.isNotEmpty()) {
                    NormalDoorbellListState(cameraList, pagedList)
                } else {
                    EmptyDoorbellListState(cameraList)
                }
            }
            .catch { cause: Throwable ->
                emit(ErrorDoorbellListState(error = cause))
            }
            .onStart {
                loadingData.postValue(true)
            }
            .onEach {
                loadingData.postValue(false)
            }

    val loadingData = MutableLiveData<Boolean>()

    fun onCameraClicked(cameraData: CameraData) {
        viewModelScope.launch {
            doorbellRepository.sendCameraImageRequest(
                deviceId = thisDeviceRepository.doorbellId(),
                cameraId = cameraData.cameraId,
                isRequested = true
            )
        }
    }

    private fun createDataSourceFactory(): DataSource.Factory<String, DoorbellData> =
        object : DataSource.Factory<String, DoorbellData>() {
            override fun create() = doorbellsDataSource
        }

    companion object {
        private const val PAGE_SIZE = 20
    }
}
