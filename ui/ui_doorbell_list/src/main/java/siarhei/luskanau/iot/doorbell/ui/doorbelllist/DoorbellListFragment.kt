package siarhei.luskanau.iot.doorbell.ui.doorbelllist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import siarhei.luskanau.iot.doorbell.ui.CameraAdapter
import siarhei.luskanau.iot.doorbell.ui.DoorbellsAdapter
import siarhei.luskanau.iot.doorbell.ui.common.BaseFragment
import siarhei.luskanau.iot.doorbell.ui.common.databinding.LayoutGenericEmptyBinding
import siarhei.luskanau.iot.doorbell.ui.common.databinding.LayoutGenericErrorBinding
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.databinding.FragmentDoorbellListBinding
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.databinding.LayoutDoorbellListNormalBinding
import timber.log.Timber

class DoorbellListFragment(
    presenterProvider: (fragment: Fragment) -> DoorbellListPresenter
) : BaseFragment<DoorbellListPresenter>(presenterProvider) {

    private lateinit var fragmentBinding: FragmentDoorbellListBinding
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
        FragmentDoorbellListBinding.inflate(
            inflater,
            container,
            false
        ).also {
            fragmentBinding = it
            fragmentBinding.pullToRefresh.setOnRefreshListener { presenter.requestData() }
            fragmentBinding.camerasRecyclerView.adapter = camerasAdapter
        }

        LayoutDoorbellListNormalBinding.inflate(
            inflater,
            container,
            false
        ).also {
            normalStateBinding = it
            normalStateBinding.doorbellsRecyclerView.adapter = doorbellsAdapter
        }

        LayoutGenericEmptyBinding.inflate(
            inflater,
            container,
            false
        ).also {
            emptyStateBinding = it
        }

        LayoutGenericErrorBinding.inflate(
            inflater,
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
        presenter.getDoorbellListStateData().observe(viewLifecycleOwner) { changeState(it) }
        presenter.getLoadingData().observe(viewLifecycleOwner) {
            fragmentBinding.pullToRefresh.isRefreshing = it
        }
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

        fragmentBinding.camerasRecyclerView.isVisible = (state is ErrorDoorbellListState).not()
        when (state) {
            is EmptyDoorbellListState -> {
                camerasAdapter.submitList(state.cameraList)
            }

            is NormalDoorbellListState -> {
                camerasAdapter.submitList(state.cameraList)
                doorbellsAdapter.submitList(state.doorbellList)
            }

            is ErrorDoorbellListState -> {
                Timber.e(state.error)
                errorStateBinding.errorMessage.text = state.error.message
            }
        }
    }
}
