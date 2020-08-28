package siarhei.luskanau.iot.doorbell.koin.common.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import org.koin.core.Koin
import org.koin.core.definition.BeanDefinition
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import timber.log.Timber

class KoinFragmentFactory(
    private val koin: Koin,
    private val appNavigation: AppNavigation
) : FragmentFactory() {

    @Suppress("TooGenericExceptionCaught")
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment =
        try {
            Timber.d("KoinFragmentFactory:instantiate:$className")
            val clazz = loadFragmentClass(classLoader, className)
            koin.get(
                clazz = clazz.kotlin,
                qualifier = null,
                parameters = { parametersOf(appNavigation) }
            )
        } catch (koinThrowable: Throwable) {
            try {
                super.instantiate(classLoader, className)
            } catch (_: Throwable) {
                throw koinThrowable
            }
        }
}

inline fun <reified T : Fragment> Module.fragment(
    noinline definition: FragmentDefinition<T>
): BeanDefinition<T> =
    factory { (appNavigation: AppNavigation) ->
        definition(appNavigation)
    }

typealias FragmentDefinition<T> = Scope.(AppNavigation) -> T
