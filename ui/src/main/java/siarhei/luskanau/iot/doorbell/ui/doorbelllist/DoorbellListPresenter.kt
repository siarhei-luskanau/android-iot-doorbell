package siarhei.luskanau.iot.doorbell.ui.doorbelllist

import androidx.lifecycle.LiveData
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData

interface DoorbellListPresenter {
    fun getDoorbellListStateData(): LiveData<DoorbellListState>
    fun getLoadingData(): LiveData<Boolean>
    fun requestData()
    fun onCameraClicked(cameraData: CameraData)
    fun onDoorbellClicked(doorbellData: DoorbellData)
}