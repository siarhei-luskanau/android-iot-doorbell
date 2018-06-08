package siarhei.luskanau.iot.doorbell.ui.doorbells

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.widget.Toast
import siarhei.luskanau.iot.doorbell.NavigationController
import siarhei.luskanau.iot.doorbell.R
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import siarhei.luskanau.iot.doorbell.databinding.FragmentDoorbellsBinding
import siarhei.luskanau.iot.doorbell.ui.base.BaseAppFragment
import siarhei.luskanau.iot.doorbell.ui.widget.CameraAdapter
import siarhei.luskanau.iot.doorbell.ui.widget.DoorbellsAdapter
import siarhei.luskanau.iot.doorbell.viewmodel.CameraImageRequestVewModel
import siarhei.luskanau.iot.doorbell.viewmodel.CamerasViewModel
import siarhei.luskanau.iot.doorbell.viewmodel.DoorbellsViewModel
import javax.inject.Inject

class DoorbellsFragment : BaseAppFragment<FragmentDoorbellsBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var navigationController: NavigationController
    @Inject
    lateinit var thisDeviceRepository: ThisDeviceRepository

    private lateinit var viewModel: DoorbellsViewModel
    private lateinit var camerasViewModel: CamerasViewModel
    private lateinit var cameraImageRequestVewModel: CameraImageRequestVewModel

    private val camerasAdapter = CameraAdapter()
    private val doorbellsAdapter = DoorbellsAdapter()
    private val deviceId: String by lazy { thisDeviceRepository.doorbellId() }

    override fun getViewLayout(): Int = R.layout.fragment_doorbells

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DoorbellsViewModel::class.java)
        camerasViewModel = ViewModelProviders.of(this, viewModelFactory).get(CamerasViewModel::class.java)
        cameraImageRequestVewModel = ViewModelProviders.of(this, viewModelFactory).get(CameraImageRequestVewModel::class.java)

        camerasAdapter.onItemClickListener = { _, _, position ->
            cameraImageRequestVewModel.deviceIdCameraIdLiveData.value =
                    Pair(deviceId, camerasAdapter.getItem(position).cameraId)
        }
        binding.camerasRecyclerView.adapter = camerasAdapter
        cameraImageRequestVewModel.cameraImageRequestLiveData.observe(this,
                Observer<String> { cameraId: String? ->
                    Toast.makeText(context, cameraId, Toast.LENGTH_SHORT).show()
                }
        )

        doorbellsAdapter.onItemClickListener = { _, _, position ->
            val doorbellData = doorbellsAdapter.currentList?.get(position)
            navigationController.navigateToImages(doorbellData?.doorbellId.orEmpty(), doorbellData?.name)
        }
        binding.doorbellsRecyclerView.adapter = doorbellsAdapter

        camerasViewModel.camerasLiveData.observe(this,
                Observer<List<CameraData>> { list: List<CameraData>? ->
                    camerasAdapter.submitList(list)
                }
        )

        viewModel.doorbellsLiveData.observe(this, Observer { pagedList -> doorbellsAdapter.submitList(pagedList) })

        camerasViewModel.deviceIdLiveData.value = deviceId
    }

}