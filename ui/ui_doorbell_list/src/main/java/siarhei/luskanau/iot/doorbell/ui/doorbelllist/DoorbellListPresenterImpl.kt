package siarhei.luskanau.iot.doorbell.ui.doorbelllist

import androidx.lifecycle.LiveData
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository

class DoorbellListPresenterImpl(
    private val doorbellListViewModel: DoorbellListViewModel,
    private val appNavigation: AppNavigation,
    private val thisDeviceRepository: ThisDeviceRepository
) : DoorbellListPresenter {

    override fun getDoorbellListStateData(): LiveData<DoorbellListState> =
        doorbellListViewModel.doorbellListStateData

    override fun getLoadingData(): LiveData<Boolean> =
        doorbellListViewModel.loadingData

    override fun requestData() {
        doorbellListViewModel.requestData()
        if (thisDeviceRepository.isPermissionsGranted().not()) {
            appNavigation.goDoorbellListToPermissions()
        }
    }

    override fun onCameraClicked(cameraData: CameraData) =
        doorbellListViewModel.onCameraClicked(cameraData)

    override fun onDoorbellClicked(doorbellData: DoorbellData) =
        appNavigation.navigateToImageList(doorbellData)
}
