package siarhei.luskanau.iot.doorbell.ui.doorbelllist

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.testing.asPagingSourceFactory
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import kotlin.test.Test

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33])
class DoorbellListComposableTest {

    @Test
    fun testNormalState() {
        val pager = Pager(
            config = PagingConfig(pageSize = 1),
            pagingSourceFactory = listOf(DoorbellData(doorbellId = "doorbellId")).asPagingSourceFactory(),
        )
        captureRoboImage(filePath = "screenshots/DoorbellListComposable.Normal.png") {
            DoorbellListComposable(
                items = pager.flow.collectAsLazyPagingItems(),
                checkPermissions = {},
                onItemClickListener = {},
            )
        }
    }

    @Test
    fun testEmptyState() {
        val pager = Pager(
            config = PagingConfig(pageSize = 1),
            pagingSourceFactory = emptyList<DoorbellData>().asPagingSourceFactory(),
        )
        captureRoboImage(filePath = "screenshots/DoorbellListComposable.Empty.png") {
            DoorbellListComposable(
                items = pager.flow.collectAsLazyPagingItems(),
                checkPermissions = {},
                onItemClickListener = {},
            )
        }
    }
}
