package siarhei.luskanau.iot.doorbell.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import org.kodein.di.DirectDI
import org.kodein.di.instance
import timber.log.Timber

class KodeinFragmentFactory(
    private val activity: FragmentActivity,
    private val injector: DirectDI
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment =
        runCatching<Fragment> {
            Timber.d("KodeinFragmentFactory:instantiate:$className")
            val clazz = loadFragmentClass(classLoader, className).kotlin
            injector.instance(tag = clazz.simpleName, arg = activity)
        }
            .getOrElse { kodeinThrowable: Throwable ->
                runCatching {
                    super.instantiate(classLoader, className)
                }
                    .getOrNull() ?: throw kodeinThrowable
            }
}
