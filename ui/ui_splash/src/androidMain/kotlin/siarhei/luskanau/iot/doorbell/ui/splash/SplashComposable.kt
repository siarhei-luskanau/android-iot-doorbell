package siarhei.luskanau.iot.doorbell.ui.splash

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import siarhei.luskanau.iot.doorbell.ui.common.R

@Composable
fun SplashComposable(
    onSplashComplete: () -> Unit,
) {
    Icon(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        painter = painterResource(
            id = R.drawable.ic_android,
        ),
        contentDescription = null,
    )
    LaunchedEffect(true) {
        onSplashComplete.invoke()
    }
}
