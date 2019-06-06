package siarhei.luskanau.iot.doorbell.kodein

import androidx.fragment.app.FragmentFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import siarhei.luskanau.iot.doorbell.navigation.NavigationActivity

class AppActivity : NavigationActivity(), KodeinAware {

    override val kodein by kodein()

    private val fragmentFactory: FragmentFactory by instance(arg = this)

    override fun getAppFragmentFactory(): FragmentFactory =
        fragmentFactory
}