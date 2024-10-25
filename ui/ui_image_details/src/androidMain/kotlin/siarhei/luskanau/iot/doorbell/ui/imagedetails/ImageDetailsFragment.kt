package siarhei.luskanau.iot.doorbell.ui.imagedetails

import androidx.fragment.app.Fragment
import siarhei.luskanau.iot.doorbell.ui.common.BaseFragment

class ImageDetailsFragment(
    presenterProvider: (fragment: Fragment) -> ImageDetailsPresenter,
) : BaseFragment<ImageDetailsPresenter>(presenterProvider) {
//            ImageDetailsComposable(
//                imageDetailsStateFlow = presenter.getImageDetailsStateFlow(),
//            )
}
