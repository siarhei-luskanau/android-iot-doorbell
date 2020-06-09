package siarhei.luskanau.iot.doorbell.ui.doorbelllist

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData

interface DoorbellListPresenter {
    fun getDoorbellListFlow(): Flow<DoorbellListState>
    fun getLoadingData(): LiveData<Boolean>
    fun requestData()
    fun checkPermissions()
    fun onDoorbellClicked(doorbellData: DoorbellData)
}
