package siarhei.luskanau.iot.doorbell.ui.imagedetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import kotlinx.coroutines.flow.Flow
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
import siarhei.luskanau.iot.doorbell.ui.common.R

@Composable
fun ImageDetailsComposable(imageDetailsStateFlow: Flow<ImageDetailsState>) {
    val imageDetailsState = imageDetailsStateFlow.collectAsState(initial = null)
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        when (val imageDetailsStateValue = imageDetailsState.value) {
            is NormalImageDetailsState -> {
                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageDetailsStateValue.imageData?.imageUri)
                        .crossfade(durationMillis = 1000)
                        .error(R.drawable.ic_error_outline)
                        .placeholder(R.drawable.ic_image)
                        .build()
                )
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .zoomable(rememberZoomState()),
                    painter = painter,
                    contentDescription = "Unsplash Image",
                    contentScale = ContentScale.Fit
                )
            }

            is ErrorImageDetailsState -> Text(
                text = imageDetailsStateValue.error.message.orEmpty(),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )

            null -> Unit
        }
    }
}
