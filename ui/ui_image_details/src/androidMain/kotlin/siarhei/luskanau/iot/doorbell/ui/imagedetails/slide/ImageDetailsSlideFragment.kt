package siarhei.luskanau.iot.doorbell.ui.imagedetails.slide

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.load
import kotlinx.coroutines.launch
import siarhei.luskanau.iot.doorbell.ui.common.BaseFragment
import siarhei.luskanau.iot.doorbell.ui.common.R
import siarhei.luskanau.iot.doorbell.ui.common.databinding.LayoutGenericErrorBinding
import siarhei.luskanau.iot.doorbell.ui.imagedetails.databinding.FragmentImageDetailsBinding
import siarhei.luskanau.iot.doorbell.ui.imagedetails.databinding.LayoutImageDetailsSlideNormalBinding
import timber.log.Timber

class ImageDetailsSlideFragment(
    presenterProvider: (fragment: Fragment) -> ImageDetailsSlidePresenter
) : BaseFragment<ImageDetailsSlidePresenter>(presenterProvider) {

    private lateinit var fragmentBinding: FragmentImageDetailsBinding
    private lateinit var normalStateBinding: LayoutImageDetailsSlideNormalBinding
    private lateinit var errorStateBinding: LayoutGenericErrorBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentBinding = FragmentImageDetailsBinding.inflate(
            inflater,
            container,
            false
        )

        normalStateBinding = LayoutImageDetailsSlideNormalBinding.inflate(
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

    override fun observeDataSources() {
        super.observeDataSources()

        viewLifecycleOwner.lifecycleScope.launch {
            presenter.getImageDetailsSlideStateFlow().collect { changeState(it) }
        }
    }

    private fun changeState(state: ImageDetailsSlideState) {
        val stateBinding = when (state) {
            is NormalImageDetailsSlideState -> normalStateBinding
            is ErrorImageDetailsSlideState -> errorStateBinding
        }

        if (fragmentBinding.containerContent.getChildAt(0) != stateBinding.root) {
            fragmentBinding.containerContent.removeAllViews()
            fragmentBinding.containerContent.addView(stateBinding.root)
        }

        when (state) {
            is NormalImageDetailsSlideState -> {
                normalStateBinding.imageView.load(state.imageData.imageUri) {
                    placeholder(R.drawable.ic_image)
                }
            }

            is ErrorImageDetailsSlideState -> {
                Timber.e(state.error)
                errorStateBinding.errorMessage.text = state.error.message
            }
        }
    }
}
