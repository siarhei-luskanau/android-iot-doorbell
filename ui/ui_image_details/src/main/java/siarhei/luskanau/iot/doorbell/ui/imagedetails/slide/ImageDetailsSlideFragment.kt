package siarhei.luskanau.iot.doorbell.ui.imagedetails.slide

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.api.load
import siarhei.luskanau.iot.doorbell.ui.common.BaseFragment
import siarhei.luskanau.iot.doorbell.ui.common.databinding.LayoutGenericErrorBinding
import siarhei.luskanau.iot.doorbell.ui.imagedetails.R
import siarhei.luskanau.iot.doorbell.ui.imagedetails.databinding.FragmentImageDetailsBinding
import siarhei.luskanau.iot.doorbell.ui.imagedetails.databinding.LayoutImageDetailsSlideNormalBinding
import timber.log.Timber

class ImageDetailsSlideFragment(
    presenterProvider: (fragment: Fragment) -> ImageDetailsSlidePresenter
) : BaseFragment<ImageDetailsSlidePresenter>(R.layout.fragment_image_details, presenterProvider) {

    private val fragmentBinding: FragmentImageDetailsBinding by viewBinding()
    private val normalStateBinding by viewBinding { fragment ->
        LayoutImageDetailsSlideNormalBinding.inflate(fragment.layoutInflater, null, false)
    }
    private val errorStateBinding by viewBinding { fragment ->
        LayoutGenericErrorBinding.inflate(fragment.layoutInflater, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.title = javaClass.simpleName
    }

    override fun observeDataSources() {
        super.observeDataSources()
        presenter.getImageDetailsSlideStateData().observe(viewLifecycleOwner) { changeState(it) }
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
