package siarhei.luskanau.iot.doorbell

import android.app.Application
import android.content.Context
import android.support.test.runner.AndroidJUnitRunner

class MockTestRunner : AndroidJUnitRunner() {

    override fun newApplication(
            classLoader: ClassLoader?,
            className: String?,
            context: Context?
    ): Application = super.newApplication(
            classLoader,
            MockAppApplication::class.qualifiedName,
            context
    )

}