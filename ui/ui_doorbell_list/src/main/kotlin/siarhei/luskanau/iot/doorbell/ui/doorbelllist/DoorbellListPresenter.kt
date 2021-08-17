package siarhei.luskanau.iot.doorbell.ui.doorbelllist

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData

interface DoorbellListPresenter {
    val doorbellListFlow: Flow<PagingData<DoorbellData>>
    fun checkPermissions()
    fun onDoorbellClicked(doorbellData: DoorbellData)
}
