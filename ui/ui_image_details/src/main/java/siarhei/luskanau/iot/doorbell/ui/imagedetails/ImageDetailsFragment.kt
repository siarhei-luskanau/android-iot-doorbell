package siarhei.luskanau.iot.doorbell.ui.imagedetails

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import by.kirich1409.viewbindingdelegate.viewBinding
import siarhei.luskanau.iot.doorbell.ui.common.BaseFragment
import siarhei.luskanau.iot.doorbell.ui.common.databinding.LayoutGenericErrorBinding
import siarhei.luskanau.iot.doorbell.ui.imagedetails.databinding.FragmentImageDetailsBinding
import siarhei.luskanau.iot.doorbell.ui.imagedetails.databinding.LayoutImageDetailsNormalBinding
import timber.log.Timber

class ImageDetailsFragment(
    presenterProvider: (fragment: Fragment) -> ImageDetailsPresenter
) : BaseFragment<ImageDetailsPresenter>(R.layout.fragment_image_details, presenterProvider) {

    private val fragmentBinding by viewBinding { fragment ->
        FragmentImageDetailsBinding.bind(fragment.requireView())
    }
    private val normalStateBinding by viewBinding { fragment ->
        LayoutImageDetailsNormalBinding.inflate(fragment.layoutInflater, null, false)
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
