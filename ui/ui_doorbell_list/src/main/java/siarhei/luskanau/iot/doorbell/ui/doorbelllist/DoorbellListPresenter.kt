package siarhei.luskanau.iot.doorbell.ui.doorbelllist

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData

interface DoorbellListPresenter {
    val doorbellListFlow: Flow<DoorbellListState>
    val loadingData: LiveData<Boolean>
    fun refreshData()
    fun checkPermissions()
    fun onDoorbellClicked(doorbellData: DoorbellData)
}
