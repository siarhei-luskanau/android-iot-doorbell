package siarhei.luskanau.iot.doorbell.ui.splash

import androidx.fragment.app.Fragment

class SplashFragment(
    presenterProvider: (fragment: Fragment) -> SplashPresenter
) : Fragment(R.layout.fragment_splash) {

    private val presenter: SplashPresenter by lazy { presenterProvider(this) }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }
}
