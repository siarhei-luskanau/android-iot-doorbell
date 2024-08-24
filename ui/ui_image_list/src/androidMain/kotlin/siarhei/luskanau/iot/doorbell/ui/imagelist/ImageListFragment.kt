package siarhei.luskanau.iot.doorbell.ui.imagelist

import android.os.Bundle
import android.view.View
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import siarhei.luskanau.iot.doorbell.ui.common.BaseFragment
import siarhei.luskanau.iot.doorbell.ui.imagelist.databinding.FragmentImageListBinding

class ImageListFragment(
    presenterProvider: (fragment: Fragment) -> ImageListPresenter,
) : BaseFragment<ImageListPresenter>(presenterProvider) {

    private lateinit var fragmentBinding: FragmentImageListBinding
    private lateinit var normalStateCompose: ComposeView

    private val camerasAdapter = CameraAdapter()

//                ImageListComposable(
//                    items = presenter.doorbellListFlow.collectAsLazyPagingItems(),
//                    onItemClickListener = { imageData ->
//                        imageData?.let { presenter.onImageClicked(it) }
//                    },
//                )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentBinding.camerasRecyclerView.adapter = camerasAdapter
        camerasAdapter.onItemClickListener = { _, _, position ->
            presenter.onCameraClicked(camerasAdapter.getItem(position))
        }
    }

    override fun observeDataSources() {
        super.observeDataSources()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                camerasAdapter.submitList(presenter.getCameraList())
            }
        }
    }
}
