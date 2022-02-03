package siarhei.luskanau.iot.doorbell.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.fragment.app.Fragment
import com.google.android.material.composethemeadapter.MdcTheme

class SplashFragment(
    presenterProvider: (fragment: Fragment) -> SplashPresenter
) : Fragment() {

    private val presenter: SplashPresenter by lazy { presenterProvider(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) =
        ComposeView(inflater.context).apply {
            setContent {
                SplashPreview()
            }
        }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    @Composable
    @Suppress("FunctionNaming")
    private fun SplashPreview() =
        MdcTheme {
            Icon(
                painter = painterResource(
                    id = siarhei.luskanau.iot.doorbell.ui.common.R.drawable.ic_android
                ),
                contentDescription = null,
            )
        }
}
