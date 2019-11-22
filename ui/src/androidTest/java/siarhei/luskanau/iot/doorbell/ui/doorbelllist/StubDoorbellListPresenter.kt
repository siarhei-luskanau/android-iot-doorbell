package siarhei.luskanau.iot.doorbell.ui.doorbelllist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData

open class StubDoorbellListPresenter : DoorbellListPresenter {

    override fun getDoorbellListStateData(): LiveData<DoorbellListState> =
        MutableLiveData()

    override fun getLoadingData(): LiveData<Boolean> =
        MutableLiveData()

    override fun requestData() {}

    override fun onCameraClicked(cameraData: CameraData) {}

    override fun onDoorbellClicked(doorbellData: DoorbellData) {}
}
