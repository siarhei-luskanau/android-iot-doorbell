package siarhei.luskanau.iot.doorbell.dagger

import android.os.Bundle
import androidx.fragment.app.FragmentFactory
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject
import siarhei.luskanau.iot.doorbell.navigation.NavigationActivity

class AppActivity : NavigationActivity(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    override fun getAppFragmentFactory(): FragmentFactory = fragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }
}
