package siarhei.luskanau.iot.doorbell.toothpick

import androidx.fragment.app.FragmentFactory
import siarhei.luskanau.iot.doorbell.navigation.NavigationActivity
import siarhei.luskanau.iot.doorbell.toothpick.di.ToothpickFragmentFactory

class AppActivity : NavigationActivity() {

    override fun getAppFragmentFactory(): FragmentFactory =
        ToothpickFragmentFactory(
            activity = this,
            scope = (application as AppApplication).scope
        )
}
