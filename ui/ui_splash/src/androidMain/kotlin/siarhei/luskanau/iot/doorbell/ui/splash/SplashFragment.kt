package siarhei.luskanau.iot.doorbell.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment

class SplashFragment(
    presenterProvider: (fragment: Fragment) -> SplashPresenter,
) : Fragment() {

    private val presenter: SplashPresenter by lazy { presenterProvider(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) =
        ComposeView(inflater.context).apply {
            setContent {
                MaterialTheme {
                    SplashComposable(
                        onSplashComplete = { presenter.onSplashComplete() },
                    )
                }
            }
        }
}
