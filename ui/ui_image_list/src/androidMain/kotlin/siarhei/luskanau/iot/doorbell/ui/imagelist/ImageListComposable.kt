package siarhei.luskanau.iot.doorbell.ui.imagelist

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.ui.common.ErrorItem
import siarhei.luskanau.iot.doorbell.ui.common.LoadingItem
import siarhei.luskanau.iot.doorbell.ui.common.R

@Composable
fun ImageListComposable(
    items: LazyPagingItems<ImageData>,
    onItemClickListener: (ImageData?) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(all = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(
            count = items.itemCount,
            key = items.itemKey { it.imageId },
            contentType = items.itemContentType { null },
        ) { index ->
            val imageData = items[index]
            imageData?.let {
                ImageDataItem(
                    imageData = imageData,
                    onItemClickListener = onItemClickListener,
                )
            }
        }
        when (val loadState = items.loadState.append) {
            is LoadState.Error -> item { ErrorItem(loadState.error.message ?: "error") }
            LoadState.Loading -> item { LoadingItem() }
            is LoadState.NotLoading -> item {
                if (items.itemCount <= 0) {
                    Text(text = "no data")
                }
            }
        }
        when (val loadState = items.loadState.refresh) {
            is LoadState.Error -> item { ErrorItem(loadState.error.message ?: "error") }
            LoadState.Loading -> item { LoadingItem() }
            is LoadState.NotLoading -> Unit
        }
    }
}

@Composable
fun ImageDataItem(
    imageData: ImageData,
    onItemClickListener: (ImageData?) -> Unit,
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageData.imageUri)
            .crossfade(durationMillis = 1000)
            .error(R.drawable.ic_error_outline)
            .placeholder(R.drawable.ic_image)
            .build(),
    )
    Surface(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable { onItemClickListener.invoke(imageData) },
        color = MaterialTheme.colorScheme.background,
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painter,
            contentDescription = "Unsplash Image",
            contentScale = ContentScale.Crop,
        )
    }
}
