package siarhei.luskanau.iot.doorbell.ui.permissions

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest

@PreviewTest
@Preview(showBackground = true)
@Composable
internal fun PermissionsComposableDenied() = PermissionsComposableDeniedPreview()

@PreviewTest
@Preview(showBackground = true)
@Composable
internal fun PermissionsComposableGranted() = PermissionsComposableGrantedPreview()

@PreviewTest
@Preview(showBackground = true)
@Composable
internal fun PermissionsComposableNotGranted() = PermissionsComposableNotGrantedPreview()
