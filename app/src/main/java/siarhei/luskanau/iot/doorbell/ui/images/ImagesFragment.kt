package siarhei.luskanau.iot.doorbell.ui.images

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import siarhei.luskanau.iot.doorbell.NavigationController
import siarhei.luskanau.iot.doorbell.R
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.databinding.FragmentImagesBinding
import siarhei.luskanau.iot.doorbell.ui.base.BaseAppFragment
import siarhei.luskanau.iot.doorbell.ui.widget.CameraAdapter
import siarhei.luskanau.iot.doorbell.ui.widget.ImageAdapter
import siarhei.luskanau.iot.doorbell.viewmodel.CameraImageRequestVewModel
import siarhei.luskanau.iot.doorbell.viewmodel.CamerasViewModel
import siarhei.luskanau.iot.doorbell.viewmodel.ImagesViewModel
import siarhei.luskanau.iot.doorbell.viewmodel.RebootRequestViewModel
import javax.inject.Inject

class ImagesFragment : BaseAppFragment<FragmentImagesBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var navigationController: NavigationController

    private lateinit var viewModel: ImagesViewModel
    private lateinit var camerasViewModel: CamerasViewModel
    private lateinit var cameraImageRequestVewModel: CameraImageRequestVewModel
    private lateinit var rebootRequestViewModel: RebootRequestViewModel

    private val camerasAdapter = CameraAdapter()
    private val imagesAdapter = ImageAdapter()

    private val deviceId: String by lazy {
        ImagesFragmentArgs.fromBundle(arguments ?: Bundle()).doorbellId
    }
    private val deviceName: String by lazy {
        ImagesFragmentArgs.fromBundle(arguments ?: Bundle()).deviceName
    }

    override fun getViewLayout(): Int = R.layout.fragment_images

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ImagesViewModel::class.java)
        camerasViewModel = ViewModelProviders.of(this, viewModelFactory).get(CamerasViewModel::class.java)
        cameraImageRequestVewModel = ViewModelProviders.of(this, viewModelFactory).get(CameraImageRequestVewModel::class.java)
        rebootRequestViewModel = ViewModelProviders.of(this, viewModelFactory).get(RebootRequestViewModel::class.java)

        camerasAdapter.onItemClickListener = { _, _, position ->
            cameraImageRequestVewModel.requestCameraImage(
                    deviceId = deviceId,
                    cameraId = camerasAdapter.getItem(position).cameraId
            )
        }
        binding.camerasRecyclerView.adapter = camerasAdapter

        imagesAdapter.onItemClickListener = { context, _, position ->
            val imageId = imagesAdapter.currentList?.get(position)?.imageId.orEmpty()
            navigationController.navigateToImageDetails(deviceId, imageId, deviceName)
            Toast.makeText(context, imageId, Toast.LENGTH_SHORT).show()
        }
        binding.imagesRecyclerView.adapter = imagesAdapter

        camerasViewModel.camerasLiveData.observe(viewLifecycleOwner,
                Observer<List<CameraData>> { list: List<CameraData>? ->
                    camerasAdapter.submitList(list)
                }
        )

        viewModel.imagesLiveData.observe(viewLifecycleOwner, Observer { imagesAdapter.submitList(it) })

        binding.uptimeView.name.setOnClickListener {
            rebootRequestViewModel.rebootDevice(deviceId, System.currentTimeMillis())
        }

        viewModel.deviceIdLiveData.value = deviceId
        camerasViewModel.deviceIdLiveData.value = deviceId
    }

    override fun getActionBarTitle(): String =
            if (deviceName.isNotBlank()) deviceName
            else deviceId
}