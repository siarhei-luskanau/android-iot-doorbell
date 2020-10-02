package siarhei.luskanau.iot.doorbell.ui.doorbelllist

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData

open class StubDoorbellListPresenter : DoorbellListPresenter {
    override val doorbellListFlow: Flow<PagingData<DoorbellData>> = emptyFlow()
    override fun checkPermissions() = Unit
    override fun onDoorbellClicked(doorbellData: DoorbellData) = Unit
}
