package siarhei.luskanau.iot.doorbell.ui.doorbelllist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.paging.Config
import androidx.paging.DataSource
import androidx.paging.toLiveData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import siarhei.luskanau.iot.doorbell.common.DoorbellsDataSource
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository

class DoorbellListViewModel(
    private val appNavigation: AppNavigation,
    private val thisDeviceRepository: ThisDeviceRepository,
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
                delay(DELAY)

                if (pagedList.isNotEmpty()) {
                    NormalDoorbellListState(pagedList)
                } else {
                    EmptyDoorbellListState
                }
            }
            .catch { cause: Throwable ->
                emit(ErrorDoorbellListState(error = cause))
            }
            .onEach { loadingData.postValue(false) }

    private val loadingData = MutableLiveData<Boolean>()
    override fun getLoadingData(): LiveData<Boolean> = loadingData

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
        private const val DELAY = 1_000L
    }
}
