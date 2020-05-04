package siarhei.luskanau.iot.doorbell.koin.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import org.koin.core.Koin
import org.koin.core.parameter.parametersOf
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import timber.log.Timber

class KoinFragmentFactory(
    private val koin: Koin,
    private val appNavigation: AppNavigation
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment =
        runCatching<Fragment> {
            Timber.d("KoinFragmentFactory:instantiate:$className")
            val clazz = loadFragmentClass(classLoader, className)
            koin.get(
                clazz = clazz.kotlin,
                qualifier = null,
                parameters = { parametersOf(appNavigation) }
            )
        }
            .getOrElse { koinThrowable: Throwable ->
                runCatching {
                    super.instantiate(classLoader, className)
                }
                    .getOrNull() ?: throw koinThrowable
            }
}
