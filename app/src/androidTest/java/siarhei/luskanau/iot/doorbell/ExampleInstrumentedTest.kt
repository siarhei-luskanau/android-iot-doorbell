package siarhei.luskanau.iot.doorbell

import androidx.test.InstrumentationRegistry
import org.junit.Test
import kotlin.test.assertEquals

class ExampleInstrumentedTest {

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals(BuildConfig.APPLICATION_ID + "", appContext.packageName)
    }

}