package siarhei.luskanau.iot.doorbell.doomain

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import kotlin.test.assertEquals

class DomainAndroidTest {

    @Test
    fun checkApplicationId() {
        assertEquals(
            expected = "siarhei.luskanau.iot.doorbell.doomain",
            actual = BuildConfig.APPLICATION_ID
        )
    }

    @Test
    fun useApplicationContext() {
        assertEquals(
            expected = "siarhei.luskanau.iot.doorbell.doomain.test",
            actual = ApplicationProvider.getApplicationContext<Context>().packageName
        )
    }

    @Test
    fun useInstrumentationContext() {
        assertEquals(
            expected = "siarhei.luskanau.iot.doorbell.doomain.test",
            actual = InstrumentationRegistry.getInstrumentation().context.packageName
        )
    }
}