package siarhei.luskanau.iot.doorbell.ui.doorbelllist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData

open class StubDoorbellListPresenter : DoorbellListPresenter {
    override val doorbellListFlow: Flow<DoorbellListState> = emptyFlow()
    override val loadingData: LiveData<Boolean> = MutableLiveData()
    override fun refreshData() = Unit
    override fun checkPermissions() = Unit
    override fun onDoorbellClicked(doorbellData: DoorbellData) = Unit
}
