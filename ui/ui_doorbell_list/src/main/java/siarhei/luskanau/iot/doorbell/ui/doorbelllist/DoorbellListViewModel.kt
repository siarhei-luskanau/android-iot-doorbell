package siarhei.luskanau.iot.doorbell.ui.doorbelllist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import siarhei.luskanau.iot.doorbell.common.DoorbellsDataSource
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository

class DoorbellListViewModel(
    private val appNavigation: AppNavigation,
    private val thisDeviceRepository: ThisDeviceRepository,
    private val doorbellsDataSource: DoorbellsDataSource
) : ViewModel(), DoorbellListPresenter {

    private var pagingDataFlow: Flow<PagingData<DoorbellData>>? = null

    private val requestLiveData = MutableLiveData(Any())

    override val doorbellListFlow: Flow<DoorbellListState> = requestLiveData.asFlow()
        .onStart { loadingData.postValue(true) }
        .flatMapLatest { getPagingDataFlow() }
        .map { pagingData ->
            delay(DELAY)
            NormalDoorbellListState(pagingData) as DoorbellListState
        }
        .catch { cause: Throwable ->
            emit(ErrorDoorbellListState(error = cause))
        }
        .onEach { loadingData.postValue(false) }

    private fun getPagingDataFlow(): Flow<PagingData<DoorbellData>> {
        if (pagingDataFlow == null) {
            pagingDataFlow = Pager(
                config = PagingConfig(pageSize = PAGE_SIZE),
                pagingSourceFactory = { doorbellsDataSource }
            ).flow
        }
        return requireNotNull(pagingDataFlow)
    }

    override val loadingData = MutableLiveData<Boolean>()

    override fun onDoorbellClicked(doorbellData: DoorbellData) {
        viewModelScope.launch {
            appNavigation.navigateToImageList(doorbellData)
        }
    }

    override fun refreshData() {
        viewModelScope.launch {
            pagingDataFlow = null
            requestLiveData.postValue(Any())
        }
    }

    override fun checkPermissions() {
        viewModelScope.launch {
            if (thisDeviceRepository.isPermissionsGranted().not()) {
                appNavigation.goDoorbellListToPermissions()
            }
        }
    }

    companion object {
        private const val PAGE_SIZE = 20
        private const val DELAY = 1_000L
    }
}
