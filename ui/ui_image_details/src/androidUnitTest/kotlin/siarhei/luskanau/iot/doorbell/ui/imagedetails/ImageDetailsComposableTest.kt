package siarhei.luskanau.iot.doorbell.ui.imagedetails

import com.github.takahirom.roborazzi.captureRoboImage
import kotlinx.coroutines.flow.flowOf
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import kotlin.test.Test

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34])
class ImageDetailsComposableTest {

    @Test
    fun testNormalState() {
        captureRoboImage(filePath = "screenshots/ImageDetailsComposable.normal.png") {
            ImageDetailsComposable(
                imageDetailsStateFlow = flowOf(
                    NormalImageDetailsState(
                        imageData = ImageData(
                            imageId = "expectedImageId",
                            imageUri = "expectedImageUri",
                            timestampString = "timestampString",
                        ),
                    ),
                ),
            )
        }
    }

    @Test
    fun testErrorState() {
        captureRoboImage(filePath = "screenshots/ImageDetailsComposable.error.png") {
            ImageDetailsComposable(
                imageDetailsStateFlow = flowOf(
                    ErrorImageDetailsState(
                        error = RuntimeException("Test Exception"),
                    ),
                ),
            )
        }
    }
}
