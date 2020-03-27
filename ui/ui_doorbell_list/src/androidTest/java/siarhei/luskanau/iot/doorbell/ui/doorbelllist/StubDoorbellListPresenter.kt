package siarhei.luskanau.iot.doorbell.ui.doorbelllist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData

open class StubDoorbellListPresenter : DoorbellListPresenter {

    override fun getDoorbellListFlow(): Flow<DoorbellListState> = emptyFlow()

    override fun getLoadingData(): LiveData<Boolean> =
        MutableLiveData()

    override fun requestData() = Unit

    override fun checkPermissions() = Unit

    override fun onCameraClicked(cameraData: CameraData) = Unit

    override fun onDoorbellClicked(doorbellData: DoorbellData) = Unit
}
