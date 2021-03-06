package siarhei.luskanau.iot.doorbell.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
// import androidx.compose.foundation.Image
// import androidx.compose.material.MaterialTheme
// import androidx.compose.runtime.Composable
// import androidx.compose.ui.layout.ContentScale
// import androidx.compose.ui.platform.ComposeView
// import androidx.compose.ui.res.painterResource
// import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment

class SplashFragment(
    presenterProvider: (fragment: Fragment) -> SplashPresenter
) : Fragment() {

    private val presenter: SplashPresenter by lazy { presenterProvider(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) =
        // ComposeView(inflater.context).apply {
        //     setContent {
        //         SplashPreview()
        //     }
        // }.let {
        View(inflater.context)
    // }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    // @Preview
    // @Composable
    // private fun SplashPreview() =
    //     MaterialTheme {
    //         Image(
    //             painter = painterResource(id = R.drawable.ic_android),
    //             contentDescription = null,
    //             contentScale = ContentScale.Fit,
    //         )
    //     }
}
