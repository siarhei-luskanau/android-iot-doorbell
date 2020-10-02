package siarhei.luskanau.iot.doorbell.ui.imagelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import siarhei.luskanau.iot.doorbell.ui.common.BaseFragment
import siarhei.luskanau.iot.doorbell.ui.common.adapter.AppLoadStateAdapter
import siarhei.luskanau.iot.doorbell.ui.common.databinding.LayoutGenericEmptyBinding
import siarhei.luskanau.iot.doorbell.ui.common.databinding.LayoutGenericErrorBinding
import siarhei.luskanau.iot.doorbell.ui.imagelist.databinding.FragmentImageListBinding
import siarhei.luskanau.iot.doorbell.ui.imagelist.databinding.LayoutImageListNormalBinding

class ImageListFragment(
    presenterProvider: (fragment: Fragment) -> ImageListPresenter
) : BaseFragment<ImageListPresenter>(presenterProvider) {

    private lateinit var fragmentBinding: FragmentImageListBinding
    private lateinit var normalStateBinding: LayoutImageListNormalBinding
    private lateinit var emptyStateBinding: LayoutGenericEmptyBinding
    private lateinit var errorStateBinding: LayoutGenericErrorBinding

    private val camerasAdapter = CameraAdapter()
    private val imageAdapter = ImageAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentBinding = FragmentImageListBinding.inflate(
            inflater,
            container,
            false
        )

        normalStateBinding = LayoutImageListNormalBinding.inflate(
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

        fragmentBinding.camerasRecyclerView.adapter = camerasAdapter
        fragmentBinding.rebootButton.setOnClickListener { presenter.rebootDevice() }
        normalStateBinding.imagesRecyclerView.adapter = imageAdapter

        camerasAdapter.onItemClickListener = { _, _, position ->
            presenter.onCameraClicked(camerasAdapter.getItem(position))
        }
        imageAdapter.onItemClickListener = { _, _, position ->
            imageAdapter.getItemAtPosition(position)?.let { presenter.onImageClicked(it) }
        }
        imageAdapter.withLoadStateHeaderAndFooter(
            header = AppLoadStateAdapter { imageAdapter.retry() },
            footer = AppLoadStateAdapter { imageAdapter.retry() }
        )
    }

    override fun observeDataSources() {
        super.observeDataSources()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            presenter.viewStateFlow.collect {
                changeViewState(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            imageAdapter.loadStateFlow.collect { changeLoadState(it) }
        }
    }

    private suspend fun changeViewState(state: ImageListState) {
        camerasAdapter.submitList(state.cameraList)
        imageAdapter.submitData(state.pagingData)
        fragmentBinding.uptimeCardView.isVisible = state.isAndroidThings
        fragmentBinding.rebootButton.isVisible = state.isAndroidThings
    }

    private fun changeLoadState(combinedLoadStates: CombinedLoadStates) {
        fragmentBinding.pullToRefresh.isRefreshing =
            combinedLoadStates.source.refresh is LoadState.Loading

        (combinedLoadStates.source.refresh as? LoadState.Error)?.let {
            fragmentBinding.camerasRecyclerView.isVisible = false
            errorStateBinding.errorMessage.text = it.error.localizedMessage
            changeState(errorStateBinding)
        } ?: run {
            fragmentBinding.camerasRecyclerView.isVisible = true
            if (combinedLoadStates.source.append.endOfPaginationReached &&
                imageAdapter.itemCount == 0
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
