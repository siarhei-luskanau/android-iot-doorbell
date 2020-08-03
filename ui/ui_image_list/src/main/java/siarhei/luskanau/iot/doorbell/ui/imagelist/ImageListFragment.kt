package siarhei.luskanau.iot.doorbell.ui.imagelist

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.flow.collect
import siarhei.luskanau.iot.doorbell.ui.common.BaseFragment
import siarhei.luskanau.iot.doorbell.ui.common.databinding.LayoutGenericEmptyBinding
import siarhei.luskanau.iot.doorbell.ui.common.databinding.LayoutGenericErrorBinding
import siarhei.luskanau.iot.doorbell.ui.imagelist.databinding.FragmentImageListBinding
import siarhei.luskanau.iot.doorbell.ui.imagelist.databinding.LayoutImageListNormalBinding
import timber.log.Timber

class ImageListFragment(
    presenterProvider: (fragment: Fragment) -> ImageListPresenter
) : BaseFragment<ImageListPresenter>(R.layout.fragment_image_list, presenterProvider) {

    private val fragmentBinding by viewBinding { fragment ->
        FragmentImageListBinding.bind(fragment.requireView())
    }
    private val normalStateBinding by viewBinding { fragment ->
        LayoutImageListNormalBinding.inflate(fragment.layoutInflater, null, false)
    }
    private val emptyStateBinding by viewBinding { fragment ->
        LayoutGenericEmptyBinding.inflate(fragment.layoutInflater, null, false)
    }
    private val errorStateBinding by viewBinding { fragment ->
        LayoutGenericErrorBinding.inflate(fragment.layoutInflater, null, false)
    }

    private val camerasAdapter = CameraAdapter()
    private val imageAdapter = ImageAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.title = javaClass.simpleName

        fragmentBinding.camerasRecyclerView.adapter = camerasAdapter
        fragmentBinding.rebootButton.setOnClickListener { presenter.rebootDevice() }
        fragmentBinding.pullToRefresh.setOnRefreshListener { presenter.refreshData() }
        normalStateBinding.imagesRecyclerView.adapter = imageAdapter

        camerasAdapter.onItemClickListener = { _, _, position ->
            presenter.onCameraClicked(camerasAdapter.getItem(position))
        }
        imageAdapter.onItemClickListener = { _, _, position ->
            imageAdapter.getItem1(position)?.let { presenter.onImageClicked(it) }
        }
    }

    override fun observeDataSources() {
        super.observeDataSources()
        lifecycleScope.launchWhenStarted {
            presenter.imageListStateFlow.collect {
                changeState(it)
            }
        }
        presenter.loadingData.observe(viewLifecycleOwner) {
            fragmentBinding.pullToRefresh.isRefreshing = it
        }
    }

    private suspend fun changeState(state: ImageListState) {
        val stateBinding = when (state) {
            is NormalImageListState -> normalStateBinding
            is EmptyImageListState -> emptyStateBinding
            is ErrorImageListState -> errorStateBinding
        }

        if (fragmentBinding.containerContent.getChildAt(0) != stateBinding.root) {
            fragmentBinding.containerContent.removeAllViews()
            fragmentBinding.containerContent.addView(stateBinding.root)
        }

        fragmentBinding.camerasRecyclerView.isVisible = (state is ErrorImageListState).not()
        when (state) {
            is EmptyImageListState -> {
                camerasAdapter.submitList(state.cameraList)
                fragmentBinding.uptimeCardView.isVisible = state.isAndroidThings
                fragmentBinding.rebootButton.isVisible = state.isAndroidThings
            }

            is NormalImageListState -> {
                camerasAdapter.submitList(state.cameraList)
                imageAdapter.submitData(state.pagingData)
                fragmentBinding.uptimeCardView.isVisible = state.isAndroidThings
                fragmentBinding.rebootButton.isVisible = state.isAndroidThings
            }

            is ErrorImageListState -> {
                Timber.e(state.error)
                errorStateBinding.errorMessage.text = state.error.message
                fragmentBinding.uptimeCardView.isVisible = false
                fragmentBinding.rebootButton.isVisible = false
            }
        }
    }
}
