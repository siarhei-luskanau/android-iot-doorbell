package siarhei.luskanau.iot.doorbell.common

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import kotlin.test.assertEquals
import org.junit.Test

class DomainAndroidTest {

    @Test
    fun checkApplicationId() {
        assertEquals(
            expected = "siarhei.luskanau.iot.doorbell.common",
            actual = BuildConfig.LIBRARY_PACKAGE_NAME
        )
    }

    @Test
    fun useApplicationContext() {
        assertEquals(
            expected = "siarhei.luskanau.iot.doorbell.common.test",
            actual = ApplicationProvider.getApplicationContext<Context>().packageName
        )
    }

    @Test
    fun useInstrumentationContext() {
        assertEquals(
            expected = "siarhei.luskanau.iot.doorbell.common.test",
            actual = InstrumentationRegistry.getInstrumentation().context.packageName
        )
    }
}
