package siarhei.luskanau.iot.doorbell.ui.permissions

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PermissionsComposable() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        text = "permissions",
        style = MaterialTheme.typography.h6,
    )
}
