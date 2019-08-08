package siarhei.luskanau.iot.doorbell.koin

import androidx.fragment.app.FragmentFactory
import org.koin.android.ext.android.get
import org.koin.core.parameter.parametersOf
import siarhei.luskanau.iot.doorbell.navigation.NavigationActivity

class AppActivity : NavigationActivity() {

    override fun getAppFragmentFactory(): FragmentFactory =
        get { parametersOf(this) }
}
