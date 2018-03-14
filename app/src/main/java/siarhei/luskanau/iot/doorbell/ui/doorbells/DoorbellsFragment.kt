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
import siarhei.luskanau.iot.doorbell.databinding.FragmentDoorbellsBinding
import siarhei.luskanau.iot.doorbell.ui.base.BaseAppFragment
import siarhei.luskanau.iot.doorbell.ui.widget.CameraAdapter
import siarhei.luskanau.iot.doorbell.ui.widget.DoorbellsAdapter
import javax.inject.Inject

class DoorbellsFragment : BaseAppFragment<FragmentDoorbellsBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var navigationController: NavigationController
    private lateinit var viewModel: DoorbellsViewModel
    private val camerasAdapter = CameraAdapter()
    private val doorbellsAdapter = DoorbellsAdapter()

    override fun getViewLayout(): Int = R.layout.fragment_doorbells

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DoorbellsViewModel::class.java)

        camerasAdapter.onItemClickListener = { _, _, position ->
            viewModel.cameraIdLiveData.value = camerasAdapter.getItem(position).cameraId
        }
        binding.camerasRecyclerView.adapter = camerasAdapter
        viewModel.cameraImageRequestLiveData.observe(this,
                Observer<String> { cameraId: String? ->
                    Toast.makeText(context, cameraId, Toast.LENGTH_SHORT).show()
                }
        )

        doorbellsAdapter.onItemClickListener = { _, _, position ->
            val doorbellData = doorbellsAdapter.getItem(position)
            navigationController.navigateToImages(doorbellData.doorbellId, doorbellData.name)
        }
        binding.doorbellsRecyclerView.adapter = doorbellsAdapter

        viewModel.camerasLiveData.observe(this,
                Observer<List<CameraData>> { list: List<CameraData>? ->
                    camerasAdapter.setItems(list)
                }
        )

        viewModel.doorbellsLiveData.observe(this,
                Observer<List<DoorbellData>> { list: List<DoorbellData>? ->
                    doorbellsAdapter.setItems(list)
                }
        )
    }

}