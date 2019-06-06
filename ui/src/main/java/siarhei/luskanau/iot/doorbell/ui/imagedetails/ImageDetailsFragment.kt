package siarhei.luskanau.iot.doorbell.ui.imagedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import siarhei.luskanau.iot.doorbell.ui.R
import siarhei.luskanau.iot.doorbell.ui.common.BaseFragment
import siarhei.luskanau.iot.doorbell.ui.databinding.LayoutGenericContentContainerBinding
import siarhei.luskanau.iot.doorbell.ui.databinding.LayoutGenericErrorBinding
import siarhei.luskanau.iot.doorbell.ui.databinding.LayoutImageDetailsNormalBinding

class ImageDetailsFragment(
    presenterProvider: (args: Bundle?) -> ImageDetailsPresenter
) : BaseFragment<ImageDetailsPresenter>(presenterProvider) {

    private lateinit var fragmentBinding: LayoutGenericContentContainerBinding
    private lateinit var normalStateBinding: LayoutImageDetailsNormalBinding
    private lateinit var errorStateBinding: LayoutGenericErrorBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DataBindingUtil.inflate<LayoutGenericContentContainerBinding>(
            inflater,
            R.layout.layout_generic_content_container,
            container,
            false
        ).also {
            fragmentBinding = it
        }

        DataBindingUtil.inflate<LayoutImageDetailsNormalBinding>(
            inflater,
            R.layout.layout_image_details_normal,
            container,
            false
        ).also {
            normalStateBinding = it
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
    }

    override fun observeDataSources() {
        super.observeDataSources()
        presenter.getImageDetailsStateData()
            .observe(viewLifecycleOwner, Observer { changeState(it) })
    }

    private fun changeState(state: ImageDetailsState) {
        val stateBinding = when (state) {
            is NormalImageDetailsState -> normalStateBinding
            is ErrorImageDetailsState -> errorStateBinding
        }

        if (fragmentBinding.containerContent.getChildAt(0) != stateBinding.root) {
            fragmentBinding.containerContent.removeAllViews()
            fragmentBinding.containerContent.addView(stateBinding.root)
        }

        when (state) {
            is NormalImageDetailsState -> Glide
                .with(requireContext())
                .load(state.imageData.imageUri)
                .into(normalStateBinding.imageView)

            is ErrorImageDetailsState -> errorStateBinding.errorMessage.text = state.error.message
        }
    }
}