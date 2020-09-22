package siarhei.luskanau.iot.doorbell.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.vectorResource
import androidx.fragment.app.Fragment
import androidx.ui.tooling.preview.Preview

class SplashFragment(
    presenterProvider: (fragment: Fragment) -> SplashPresenter
) : Fragment() {

    private val presenter: SplashPresenter by lazy { presenterProvider(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) =
        ComposeView(requireContext()).apply {
            setContent {
                SplashPreview()
            }
        }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    @Preview
    @Composable
    private fun SplashPreview() =
        MaterialTheme {
            Image(
                asset = vectorResource(id = R.drawable.ic_android),
                contentScale = ContentScale.Fit
            )
        }
}
