package siarhei.luskanau.iot.doorbell

import androidx.fragment.app.FragmentFactory
import siarhei.luskanau.iot.doorbell.navigation.NavigationActivity

class AppActivity : NavigationActivity() {

    override fun getAppFragmentFactory(): FragmentFactory =
        AppFragmentFactory(
            activity = this,
            appModules = (application as AppApplication).appModules
        )
}
