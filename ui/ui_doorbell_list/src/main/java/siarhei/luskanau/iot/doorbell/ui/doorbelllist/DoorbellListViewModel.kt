package siarhei.luskanau.iot.doorbell.ui.doorbelllist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
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

    override val doorbellListFlow: Flow<PagingData<DoorbellData>> =
        Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = { doorbellsDataSource }
        )
            .flow
            .cachedIn(viewModelScope)

    override fun onDoorbellClicked(doorbellData: DoorbellData) {
        viewModelScope.launch {
            appNavigation.navigateToImageList(doorbellData)
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
        private const val PAGE_SIZE = 2
    }
}
