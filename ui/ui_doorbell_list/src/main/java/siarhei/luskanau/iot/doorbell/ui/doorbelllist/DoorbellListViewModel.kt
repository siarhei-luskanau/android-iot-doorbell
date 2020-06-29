package siarhei.luskanau.iot.doorbell.ui.doorbelllist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
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
        Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = { doorbellsDataSource }
        )
            .flow
            .onEach { loadingData.postValue(true) }
            .map { pagingData ->
                delay(DELAY)
                NormalDoorbellListState(pagingData)
            }
//            .catch { cause: Throwable ->
//                emit(ErrorDoorbellListState(error = cause))
//            }
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

    companion object {
        private const val PAGE_SIZE = 20
        private const val DELAY = 1_000L
    }
}
