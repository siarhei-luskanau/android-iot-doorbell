package siarhei.luskanau.iot.doorbell.ui.images

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.widget.Toast
import siarhei.luskanau.iot.doorbell.AppConstants
import siarhei.luskanau.iot.doorbell.AppConstants.DATE_FORMAT
import siarhei.luskanau.iot.doorbell.R
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.databinding.FragmentImagesBinding
import siarhei.luskanau.iot.doorbell.ui.base.BaseAppFragment
import siarhei.luskanau.iot.doorbell.ui.widget.CameraAdapter
import siarhei.luskanau.iot.doorbell.ui.widget.ImageAdapter
import javax.inject.Inject

class ImagesFragment : BaseAppFragment<FragmentImagesBinding>() {

    companion object {
        fun create(deviceId: String, deviceName: String?): ImagesFragment {
            val args = Bundle()
            args.putString(AppConstants.DEVICE_ID, deviceId)
            args.putString(AppConstants.NAME, deviceName)

            val fragment = ImagesFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: ImagesViewModel
    private val camerasAdapter = CameraAdapter()
    private val imagesAdapter = ImageAdapter()

    private val deviceId: String by lazy { arguments?.getString(AppConstants.DEVICE_ID) ?: "" }
    private val deviceName: String by lazy { arguments?.getString(AppConstants.NAME) ?: "" }

    override fun getViewLayout(): Int = R.layout.fragment_images

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ImagesViewModel::class.java)

        camerasAdapter.onItemClickListener = { _, _, position ->
            viewModel.cameraIdLiveData.value = camerasAdapter.getItem(position).cameraId
        }
        binding.camerasRecyclerView.adapter = camerasAdapter
        viewModel.cameraImageRequestLiveData.observe(this,
                Observer<String> { cameraId: String? ->
                    Toast.makeText(context, cameraId, Toast.LENGTH_SHORT).show()
                }
        )

        imagesAdapter.onItemClickListener = { context, _, position ->
            Toast.makeText(context, imagesAdapter.getItem(position).imageId, Toast.LENGTH_SHORT).show()
        }
        binding.imagesRecyclerView.adapter = imagesAdapter

        viewModel.camerasLiveData.observe(this,
                Observer<List<CameraData>> { list: List<CameraData>? ->
                    camerasAdapter.setItems(list)
                }
        )

        viewModel.imagesLiveData.observe(this,
                Observer<List<ImageData>> { list: List<ImageData>? ->
                    imagesAdapter.setItems(list)
                }
        )

        binding.uptimeView?.name?.setOnClickListener({
            viewModel.uptimeRebootRequestClickLiveData.value = System.currentTimeMillis()
        })
        viewModel.uptimeRebootRequestUpdateLiveData.observe(this,
                Observer<Long> {
                    Toast.makeText(context, DATE_FORMAT.format(it), Toast.LENGTH_SHORT).show()
                }
        )

        viewModel.deviceIdLiveData.value = deviceId
    }

    override fun getActionBarTitle(): String =
            if (deviceName.isNotBlank()) deviceName
            else deviceId

}