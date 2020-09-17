package siarhei.luskanau.iot.doorbell.ui.doorbelllist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
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

    private val doorbellsAdapter = DoorbellsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentBinding = FragmentDoorbellListBinding.inflate(
            inflater,
            container,
            false
        )

        normalStateBinding = LayoutDoorbellListNormalBinding.inflate(
            inflater,
            container,
            false
        )

        emptyStateBinding = LayoutGenericEmptyBinding.inflate(
            inflater,
            container,
            false
        )

        errorStateBinding = LayoutGenericErrorBinding.inflate(
            inflater,
            container,
            false
        )

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.title = javaClass.simpleName

        fragmentBinding.pullToRefresh.setOnRefreshListener { presenter.refreshData() }
        normalStateBinding.doorbellsRecyclerView.adapter = doorbellsAdapter
        doorbellsAdapter.onItemClickListener = { _, _, position ->
            doorbellsAdapter.getItem1(position)?.let { presenter.onDoorbellClicked(it) }
        }

        presenter.checkPermissions()
    }

    override fun observeDataSources() {
        super.observeDataSources()
        lifecycleScope.launchWhenStarted {
            presenter.doorbellListFlow.collect {
                changeState(it)
            }
        }
        presenter.loadingData.observe(viewLifecycleOwner) {
            fragmentBinding.pullToRefresh.isRefreshing = it
        }
    }

    private suspend fun changeState(state: DoorbellListState) {
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
            }

            is NormalDoorbellListState -> {
                doorbellsAdapter.submitData(state.pagingData)
            }

            is ErrorDoorbellListState -> {
                Timber.e(state.error)
                errorStateBinding.errorMessage.text = state.error.message
            }
        }
    }
}
