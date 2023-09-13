package siarhei.luskanau.iot.doorbell.ui.imagelist

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.testing.asPagingSourceFactory
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import kotlin.test.Test

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33])
class ImageListComposableTest {

    @Test
    fun testNormalState() {
        val pager = Pager(
            config = PagingConfig(pageSize = 1),
            pagingSourceFactory = listOf(
                ImageData(
                    imageId = "1",
                    imageUri = null,
                ),
            ).asPagingSourceFactory(),
        )
        captureRoboImage(filePath = "screenshots/ImageListComposable.Normal.png") {
            ImageListComposable(
                items = pager.flow.collectAsLazyPagingItems(),
                onItemClickListener = {},
            )
        }
    }

    @Test
    fun testEmptyState() {
        val pager = Pager(
            config = PagingConfig(pageSize = 1),
            pagingSourceFactory = emptyList<ImageData>().asPagingSourceFactory(),
        )
        captureRoboImage(filePath = "screenshots/ImageListComposable.Empty.png") {
            ImageListComposable(
                items = pager.flow.collectAsLazyPagingItems(),
                onItemClickListener = {},
            )
        }
    }
}
