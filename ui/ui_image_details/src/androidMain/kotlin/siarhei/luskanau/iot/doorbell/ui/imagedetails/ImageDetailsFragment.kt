package siarhei.luskanau.iot.doorbell.ui.imagedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import siarhei.luskanau.iot.doorbell.ui.common.BaseFragment

class ImageDetailsFragment(
    presenterProvider: (fragment: Fragment) -> ImageDetailsPresenter,
) : BaseFragment<ImageDetailsPresenter>(presenterProvider) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View =
        ComposeView(inflater.context).apply {
            setContent {
                MaterialTheme {
                    ImageDetailsComposable(
                        imageDetailsStateFlow = presenter.getImageDetailsStateFlow(),
                    )
                }
            }
        }
}
