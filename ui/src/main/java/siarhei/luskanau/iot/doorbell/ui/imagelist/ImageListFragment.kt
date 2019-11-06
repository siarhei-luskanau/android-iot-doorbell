package siarhei.luskanau.iot.doorbell.ui.imagelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import siarhei.luskanau.iot.doorbell.ui.CameraAdapter
import siarhei.luskanau.iot.doorbell.ui.ImageAdapter
import siarhei.luskanau.iot.doorbell.ui.common.BaseFragment
import siarhei.luskanau.iot.doorbell.ui.databinding.FragmentGenericRefreshContentContainerBinding
import siarhei.luskanau.iot.doorbell.ui.databinding.LayoutGenericEmptyBinding
import siarhei.luskanau.iot.doorbell.ui.databinding.LayoutGenericErrorBinding
import siarhei.luskanau.iot.doorbell.ui.databinding.LayoutImageListNormalBinding

class ImageListFragment(
    presenterProvider: (args: Bundle?) -> ImageListPresenter
) : BaseFragment<ImageListPresenter>(presenterProvider) {

    private lateinit var fragmentBinding: FragmentGenericRefreshContentContainerBinding
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
        FragmentGenericRefreshContentContainerBinding.inflate(
            inflater,
            container,
            false
        ).also {
            fragmentBinding = it
            fragmentBinding.pullToRefresh.setOnRefreshListener { presenter.requestData() }
        }

        LayoutImageListNormalBinding.inflate(
            inflater,
            container,
            false
        ).also {
            normalStateBinding = it
            normalStateBinding.camerasRecyclerView.adapter = camerasAdapter
            normalStateBinding.imagesRecyclerView.adapter = imageAdapter
            normalStateBinding.rebootButton.setOnClickListener { presenter.rebootDevice() }
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
        imageAdapter.onItemClickListener = { _, _, position ->
            imageAdapter.currentList?.get(position)?.let { presenter.onImageClicked(it) }
        }

        presenter.requestData()
    }

    override fun observeDataSources() {
        super.observeDataSources()
        presenter.getImageListStateData().observe(viewLifecycleOwner, Observer { changeState(it) })
        presenter.getLoadingData().observe(
            viewLifecycleOwner,
            Observer { fragmentBinding.pullToRefresh.isRefreshing = it })
    }

    private fun changeState(state: ImageListState) {
        val stateBinding = when (state) {
            is NormalImageListState -> normalStateBinding
            is EmptyImageListState -> emptyStateBinding
            is ErrorImageListState -> errorStateBinding
        }

        if (fragmentBinding.containerContent.getChildAt(0) != stateBinding.root) {
            fragmentBinding.containerContent.removeAllViews()
            fragmentBinding.containerContent.addView(stateBinding.root)
        }

        when (state) {
            is EmptyImageListState -> {
                camerasAdapter.submitList(state.cameraList)
            }

            is NormalImageListState -> {
                camerasAdapter.submitList(state.cameraList)
                imageAdapter.submitList(state.imageList)
            }

            is ErrorImageListState -> errorStateBinding.errorMessage.text = state.error.message
        }
    }
}
