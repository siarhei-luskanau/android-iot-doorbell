package siarhei.luskanau.iot.doorbell

import androidx.test.platform.app.InstrumentationRegistry
import kotlin.test.Test
import kotlin.test.assertEquals

class ExampleInstrumentedTest {

    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("siarhei.luskanau.iot.doorbell", appContext.packageName)
    }
}
