package siarhei.luskanau.iot.doorbell.ui.doorbelllist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.launch
import siarhei.luskanau.iot.doorbell.ui.common.BaseFragment
import siarhei.luskanau.iot.doorbell.ui.common.adapter.AppLoadStateAdapter
import siarhei.luskanau.iot.doorbell.ui.common.databinding.LayoutGenericEmptyBinding
import siarhei.luskanau.iot.doorbell.ui.common.databinding.LayoutGenericErrorBinding
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.databinding.FragmentDoorbellListBinding
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.databinding.LayoutDoorbellListNormalBinding

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
    ): View {
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

        fragmentBinding.containerContent.addView(normalStateBinding.root)
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        normalStateBinding.doorbellsRecyclerView.addItemDecoration(decoration)

        fragmentBinding.pullToRefresh.setOnRefreshListener {
            doorbellsAdapter.refresh()
            collectLoadStateFlow()
        }
        normalStateBinding.doorbellsRecyclerView.adapter =
            doorbellsAdapter.withLoadStateHeaderAndFooter(
                header = AppLoadStateAdapter { doorbellsAdapter.retry() },
                footer = AppLoadStateAdapter { doorbellsAdapter.retry() }
            )

        doorbellsAdapter.onItemClickListener = { _, _, position ->
            doorbellsAdapter.getItemAtPosition(position)?.let { presenter.onDoorbellClicked(it) }
        }

        presenter.checkPermissions()
    }

    override fun observeDataSources() {
        super.observeDataSources()

        viewLifecycleOwner.lifecycleScope.launch {
            presenter.doorbellListFlow.collect {
                doorbellsAdapter.submitData(it)
            }
        }

        collectLoadStateFlow()
    }

    private fun collectLoadStateFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            doorbellsAdapter.loadStateFlow.collect { changeLoadState(it) }
        }
    }

    private fun changeLoadState(combinedLoadStates: CombinedLoadStates) {
        fragmentBinding.pullToRefresh.isRefreshing =
            combinedLoadStates.source.refresh is LoadState.Loading

        (combinedLoadStates.source.refresh as? LoadState.Error)?.let {
            errorStateBinding.errorMessage.text = it.error.localizedMessage
            changeState(errorStateBinding)
        } ?: run {
            if (combinedLoadStates.source.append.endOfPaginationReached &&
                doorbellsAdapter.itemCount == 0
            ) {
                changeState(emptyStateBinding)
            } else {
                changeState(normalStateBinding)
            }
        }
    }

    private fun changeState(binding: ViewBinding) {
        if (fragmentBinding.containerContent.getChildAt(0) != binding.root) {
            fragmentBinding.containerContent.removeAllViews()
            fragmentBinding.containerContent.addView(binding.root)
        }
    }
}
