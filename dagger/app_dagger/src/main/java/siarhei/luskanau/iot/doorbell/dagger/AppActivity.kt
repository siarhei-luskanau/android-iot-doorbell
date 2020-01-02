package siarhei.luskanau.iot.doorbell.dagger

import androidx.fragment.app.FragmentFactory
import siarhei.luskanau.iot.doorbell.navigation.NavigationActivity

class AppActivity : NavigationActivity() {

    override fun getAppFragmentFactory(): FragmentFactory =
        (application as HasFragmentFactory).buildFragmentFactory(this)
}
