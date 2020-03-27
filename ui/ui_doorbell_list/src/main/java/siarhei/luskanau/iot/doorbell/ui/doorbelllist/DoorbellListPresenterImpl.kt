package siarhei.luskanau.iot.doorbell.ui.doorbelllist

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository

class DoorbellListPresenterImpl(
    private val doorbellListViewModel: DoorbellListViewModel,
    private val appNavigation: AppNavigation,
    private val thisDeviceRepository: ThisDeviceRepository
) : DoorbellListPresenter {

    override fun getDoorbellListFlow(): Flow<DoorbellListState> =
        doorbellListViewModel.doorbellListStateFlow

    override fun getLoadingData(): LiveData<Boolean> =
        doorbellListViewModel.loadingData

    override fun requestData() = Unit

    override fun checkPermissions() {
        if (thisDeviceRepository.isPermissionsGranted().not()) {
            appNavigation.goDoorbellListToPermissions()
        }
    }

    override fun onCameraClicked(cameraData: CameraData) =
        doorbellListViewModel.onCameraClicked(cameraData)

    override fun onDoorbellClicked(doorbellData: DoorbellData) =
        appNavigation.navigateToImageList(doorbellData)
}
