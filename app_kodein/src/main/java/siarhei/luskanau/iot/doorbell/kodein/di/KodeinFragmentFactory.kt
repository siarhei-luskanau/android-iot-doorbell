package siarhei.luskanau.iot.doorbell.kodein.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import org.kodein.di.DKodein
import org.kodein.di.generic.M
import org.kodein.di.generic.instance
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import timber.log.Timber

class KodeinFragmentFactory(
    private val activity: FragmentActivity,
    private val appNavigation: AppNavigation,
    private val injector: DKodein
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        Timber.d("KodeinFragmentFactory:instantiate:$className")
        return try {
            val clazz = loadFragmentClass(classLoader, className).kotlin
            return injector.instance(tag = clazz.simpleName, arg = M(activity, appNavigation))
        } catch (kodeinThrowable: Throwable) {
            try {
                super.instantiate(classLoader, className)
            } catch (t: Throwable) {
                throw kodeinThrowable
            }
        }
    }
}
