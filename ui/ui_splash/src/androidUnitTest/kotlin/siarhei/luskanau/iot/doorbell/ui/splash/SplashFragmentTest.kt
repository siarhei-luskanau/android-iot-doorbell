package siarhei.luskanau.iot.doorbell.ui.splash

import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import kotlin.test.Test

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33])
class SplashFragmentTest {

    @Test
    fun testNormalState() {
        captureRoboImage(filePath = "screenshots/SplashPreview.png") {
            SplashPreview()
        }
    }
}
