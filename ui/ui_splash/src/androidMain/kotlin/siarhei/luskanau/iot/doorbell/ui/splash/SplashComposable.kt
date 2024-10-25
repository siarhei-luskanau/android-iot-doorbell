package siarhei.luskanau.iot.doorbell.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import siarhei.luskanau.iot.doorbell.ui.common.theme.AppTheme
import siarhei.luskanau.iot.doorbell.ui.common.R as UiCommonR

@Composable
fun SplashComposable(viewModel: SplashViewModel) {
    Image(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        painter = painterResource(id = UiCommonR.drawable.ic_android),
        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.secondary),
        contentDescription = null,
    )
    LaunchedEffect(true) {
        viewModel.onSplashComplete()
    }
}

@Preview
@Composable
internal fun SplashComposablePreview() {
    AppTheme { SplashComposable(splashViewModel()) }
}

internal fun splashViewModel(): SplashViewModel = object : SplashViewModel() {
    override fun onSplashComplete() = Unit
}
