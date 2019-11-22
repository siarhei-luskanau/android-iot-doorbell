package siarhei.luskanau.iot.doorbell.ui.imagedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import coil.api.load
import siarhei.luskanau.iot.doorbell.ui.R
import siarhei.luskanau.iot.doorbell.ui.common.BaseFragment
import siarhei.luskanau.iot.doorbell.ui.databinding.FragmentImageDetailsBinding
import siarhei.luskanau.iot.doorbell.ui.databinding.LayoutGenericErrorBinding
import siarhei.luskanau.iot.doorbell.ui.databinding.LayoutImageDetailsNormalBinding
import timber.log.Timber

class ImageDetailsFragment(
    presenterProvider: (args: Bundle?) -> ImageDetailsPresenter
) : BaseFragment<ImageDetailsPresenter>(presenterProvider) {

    private lateinit var fragmentBinding: FragmentImageDetailsBinding
    private lateinit var normalStateBinding: LayoutImageDetailsNormalBinding
    private lateinit var errorStateBinding: LayoutGenericErrorBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        FragmentImageDetailsBinding.inflate(
            inflater,
            container,
            false
        ).also {
            fragmentBinding = it
        }

        LayoutImageDetailsNormalBinding.inflate(
            inflater,
            container,
            false
        ).also {
            normalStateBinding = it
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
            is NormalImageDetailsState -> {
                normalStateBinding.imageView.load(state.imageData.imageUri) {
                    placeholder(R.drawable.ic_image)
                }
            }

            is ErrorImageDetailsState -> {
                Timber.e(state.error)
                errorStateBinding.errorMessage.text = state.error.message
            }
        }
    }
}
