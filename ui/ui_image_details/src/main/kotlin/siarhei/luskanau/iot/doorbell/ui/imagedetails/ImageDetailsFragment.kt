package siarhei.luskanau.iot.doorbell.ui.imagedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import siarhei.luskanau.iot.doorbell.ui.common.BaseFragment
import siarhei.luskanau.iot.doorbell.ui.common.databinding.LayoutGenericErrorBinding
import siarhei.luskanau.iot.doorbell.ui.imagedetails.databinding.FragmentImageDetailsBinding
import siarhei.luskanau.iot.doorbell.ui.imagedetails.databinding.LayoutImageDetailsNormalBinding
import timber.log.Timber

class ImageDetailsFragment(
    presenterProvider: (fragment: Fragment) -> ImageDetailsPresenter
) : BaseFragment<ImageDetailsPresenter>(presenterProvider) {

    private lateinit var fragmentBinding: FragmentImageDetailsBinding
    private lateinit var normalStateBinding: LayoutImageDetailsNormalBinding
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

        normalStateBinding = LayoutImageDetailsNormalBinding.inflate(
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
        presenter.getImageDetailsStateData().observe(viewLifecycleOwner) { changeState(it) }
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
                normalStateBinding.viewPager2.adapter = state.adapter
            }

            is ErrorImageDetailsState -> {
                Timber.e(state.error)
                errorStateBinding.errorMessage.text = state.error.message
            }
        }
    }
}
