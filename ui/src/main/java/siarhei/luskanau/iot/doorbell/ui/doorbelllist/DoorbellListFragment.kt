package siarhei.luskanau.iot.doorbell.ui.doorbelllist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import siarhei.luskanau.iot.doorbell.ui.CameraAdapter
import siarhei.luskanau.iot.doorbell.ui.DoorbellsAdapter
import siarhei.luskanau.iot.doorbell.ui.R
import siarhei.luskanau.iot.doorbell.ui.common.BaseFragment
import siarhei.luskanau.iot.doorbell.ui.databinding.LayoutDoorbellListNormalBinding
import siarhei.luskanau.iot.doorbell.ui.databinding.LayoutGenericEmptyBinding
import siarhei.luskanau.iot.doorbell.ui.databinding.LayoutGenericErrorBinding
import siarhei.luskanau.iot.doorbell.ui.databinding.LayoutGenericRefreshContentContainerBinding

class DoorbellListFragment(
    presenterProvider: (args: Bundle?) -> DoorbellListPresenter
) : BaseFragment<DoorbellListPresenter>(presenterProvider) {

    private lateinit var fragmentBinding: LayoutGenericRefreshContentContainerBinding
    private lateinit var normalStateBinding: LayoutDoorbellListNormalBinding
    private lateinit var emptyStateBinding: LayoutGenericEmptyBinding
    private lateinit var errorStateBinding: LayoutGenericErrorBinding

    private val camerasAdapter = CameraAdapter()
    private val doorbellsAdapter = DoorbellsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DataBindingUtil.inflate<LayoutGenericRefreshContentContainerBinding>(
            inflater,
            R.layout.layout_generic_refresh_content_container,
            container,
            false
        ).also {
            fragmentBinding = it
            fragmentBinding.pullToRefresh.setOnRefreshListener { presenter.requestData() }
        }

        DataBindingUtil.inflate<LayoutDoorbellListNormalBinding>(
            inflater,
            R.layout.layout_doorbell_list_normal,
            container,
            false
        ).also {
            normalStateBinding = it
            normalStateBinding.camerasRecyclerView.adapter = camerasAdapter
            normalStateBinding.doorbellsRecyclerView.adapter = doorbellsAdapter
        }

        DataBindingUtil.inflate<LayoutGenericEmptyBinding>(
            inflater,
            R.layout.layout_generic_empty,
            container,
            false
        ).also {
            emptyStateBinding = it
        }

        DataBindingUtil.inflate<LayoutGenericErrorBinding>(
            inflater,
            R.layout.layout_generic_error,
            container,
            false
        ).also {
            errorStateBinding = it
        }

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.title = javaClass.simpleName

        camerasAdapter.onItemClickListener = { _, _, position ->
            presenter.onCameraClicked(camerasAdapter.getItem(position))
        }
        doorbellsAdapter.onItemClickListener = { _, _, position ->
            doorbellsAdapter.currentList?.get(position)?.let { presenter.onDoorbellClicked(it) }
        }

        presenter.requestData()
    }

    override fun observeDataSources() {
        super.observeDataSources()
        presenter.getDoorbellListStateData()
            .observe(viewLifecycleOwner, Observer { changeState(it) })
        presenter.getLoadingData().observe(
            viewLifecycleOwner,
            Observer { fragmentBinding.pullToRefresh.isRefreshing = it })
    }

    private fun changeState(state: DoorbellListState) {
        val stateBinding = when (state) {
            is NormalDoorbellListState -> normalStateBinding
            is EmptyDoorbellListState -> emptyStateBinding
            is ErrorDoorbellListState -> errorStateBinding
        }

        if (fragmentBinding.containerContent.getChildAt(0) != stateBinding.root) {
            fragmentBinding.containerContent.removeAllViews()
            fragmentBinding.containerContent.addView(stateBinding.root)
        }

        when (state) {
            is EmptyDoorbellListState -> {
                camerasAdapter.submitList(state.cameraList)
            }

            is NormalDoorbellListState -> {
                camerasAdapter.submitList(state.cameraList)
                doorbellsAdapter.submitList(state.doorbellList)
            }

            is ErrorDoorbellListState -> errorStateBinding.errorMessage.text = state.error.message
        }
    }
}