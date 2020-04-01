package siarhei.luskanau.iot.doorbell.ui.doorbelllist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.Config
import androidx.paging.DataSource
import androidx.paging.toLiveData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import siarhei.luskanau.iot.doorbell.common.DoorbellsDataSource
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.repository.CameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository

class DoorbellListViewModel(
    private val appNavigation: AppNavigation,
    private val doorbellRepository: DoorbellRepository,
    private val thisDeviceRepository: ThisDeviceRepository,
    private val cameraRepository: CameraRepository,
    private val doorbellsDataSource: DoorbellsDataSource
) : ViewModel(), DoorbellListPresenter {

    override fun getDoorbellListFlow(): Flow<DoorbellListState> =
        createDataSourceFactory()
            .toLiveData(
                config = Config(
                    pageSize = PAGE_SIZE,
                    prefetchDistance = PAGE_SIZE,
                    initialLoadSizeHint = PAGE_SIZE
                )
            )
            .asFlow()
            .onEach { loadingData.postValue(true) }
            .map { pagedList ->
                delay(1_000L)

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
            .onEach { loadingData.postValue(false) }

    private val loadingData = MutableLiveData<Boolean>()
    override fun getLoadingData(): LiveData<Boolean> = loadingData

    override fun onCameraClicked(cameraData: CameraData) {
        viewModelScope.launch {
            doorbellRepository.sendCameraImageRequest(
                deviceId = thisDeviceRepository.doorbellId(),
                cameraId = cameraData.cameraId,
                isRequested = true
            )
        }
    }

    override fun onDoorbellClicked(doorbellData: DoorbellData) =
        appNavigation.navigateToImageList(doorbellData)

    override fun requestData() = Unit

    override fun checkPermissions() {
        if (thisDeviceRepository.isPermissionsGranted().not()) {
            appNavigation.goDoorbellListToPermissions()
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
