package siarhei.luskanau.iot.doorbell.ui.doorbelllist

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.flow.collect
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
) : BaseFragment<DoorbellListPresenter>(R.layout.fragment_doorbell_list, presenterProvider) {

    private val fragmentBinding by viewBinding { fragment ->
        FragmentDoorbellListBinding.bind(fragment.requireView())
    }
    private val normalStateBinding by viewBinding { fragment ->
        LayoutDoorbellListNormalBinding.inflate(fragment.layoutInflater, null, false)
    }
    private val emptyStateBinding by viewBinding { fragment ->
        LayoutGenericEmptyBinding.inflate(fragment.layoutInflater, null, false)
    }
    private val errorStateBinding by viewBinding { fragment ->
        LayoutGenericErrorBinding.inflate(fragment.layoutInflater, null, false)
    }

    private val camerasAdapter = CameraAdapter()
    private val doorbellsAdapter = DoorbellsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.title = javaClass.simpleName

        fragmentBinding.pullToRefresh.setOnRefreshListener { presenter.requestData() }
        fragmentBinding.camerasRecyclerView.adapter = camerasAdapter
        normalStateBinding.doorbellsRecyclerView.adapter = doorbellsAdapter

        camerasAdapter.onItemClickListener = { _, _, position ->
            presenter.onCameraClicked(camerasAdapter.getItem(position))
        }
        doorbellsAdapter.onItemClickListener = { _, _, position ->
            doorbellsAdapter.currentList?.get(position)?.let { presenter.onDoorbellClicked(it) }
        }

        presenter.checkPermissions()
    }

    override fun observeDataSources() {
        super.observeDataSources()
        lifecycleScope.launchWhenStarted {
            presenter.getDoorbellListFlow().collect {
                changeState(it)
            }
        }
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
