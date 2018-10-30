package siarhei.luskanau.iot.doorbell

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Test
import kotlin.test.assertEquals

class ExampleInstrumentedTest {

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext: Context = ApplicationProvider.getApplicationContext()
        assertEquals(BuildConfig.APPLICATION_ID + "", appContext.packageName)
    }

}