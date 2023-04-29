package siarhei.luskanau.iot.doorbell.ui.imagelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.ui.common.BaseFragment
import siarhei.luskanau.iot.doorbell.ui.common.ErrorItem
import siarhei.luskanau.iot.doorbell.ui.common.LoadingItem
import siarhei.luskanau.iot.doorbell.ui.common.R
import siarhei.luskanau.iot.doorbell.ui.imagelist.databinding.FragmentImageListBinding

class ImageListFragment(
    presenterProvider: (fragment: Fragment) -> ImageListPresenter,
) : BaseFragment<ImageListPresenter>(presenterProvider) {

    private lateinit var fragmentBinding: FragmentImageListBinding
    private lateinit var normalStateCompose: ComposeView

    private val camerasAdapter = CameraAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        fragmentBinding = FragmentImageListBinding.inflate(
            inflater,
            container,
            false,
        )
        normalStateCompose = ComposeView(inflater.context).apply {
            setContent {
                MaterialTheme {
                    ImageListComposable(
                        items = presenter.doorbellListFlow.collectAsLazyPagingItems(),
                        onItemClickListener = { imageData ->
                            imageData?.let { presenter.onImageClicked(it) }
                        },
                    )
                }
            }
        }
        fragmentBinding.containerContent.addView(normalStateCompose)
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentBinding.camerasRecyclerView.adapter = camerasAdapter
        camerasAdapter.onItemClickListener = { _, _, position ->
            presenter.onCameraClicked(camerasAdapter.getItem(position))
        }
    }

    override fun observeDataSources() {
        super.observeDataSources()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                camerasAdapter.submitList(presenter.getCameraList())
            }
        }
    }
}

@Composable
@Suppress("FunctionNaming", "EmptyFunctionBlock", "UnusedPrivateMember")
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
            items = items,
            key = { imageData ->
                imageData.imageId
            },
        ) { imageData ->
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
@Suppress("FunctionNaming")
fun ImageDataItem(
    imageData: ImageData,
    onItemClickListener: (ImageData?) -> Unit,
) {
    val painter =
        rememberAsyncImagePainter(
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
        color = MaterialTheme.colors.background,
        elevation = 2.dp,
        border = if (MaterialTheme.colors.isLight) {
            null
        } else {
            BorderStroke(1.dp, MaterialTheme.colors.surface)
        },
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painter,
            contentDescription = "Unsplash Image",
            contentScale = ContentScale.Crop,
        )
    }
}
