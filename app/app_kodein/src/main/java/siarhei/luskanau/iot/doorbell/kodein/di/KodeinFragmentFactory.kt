package siarhei.luskanau.iot.doorbell.kodein.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import org.kodein.di.DirectDI
import org.kodein.di.instance
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import timber.log.Timber

class KodeinFragmentFactory(
    private val appNavigation: AppNavigation,
    private val injector: DirectDI
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment =
        runCatching<Fragment> {
            Timber.d("KodeinFragmentFactory:instantiate:$className")
            val clazz = loadFragmentClass(classLoader, className).kotlin
            injector.instance(tag = clazz.simpleName, arg = appNavigation)
        }
            .getOrElse { kodeinThrowable: Throwable ->
                runCatching {
                    super.instantiate(classLoader, className)
                }
                    .getOrNull() ?: throw kodeinThrowable
            }
}
