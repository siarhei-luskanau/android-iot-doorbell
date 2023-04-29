package siarhei.luskanau.iot.doorbell.koin.common.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import org.koin.core.Koin
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import timber.log.Timber

class KoinFragmentFactory(
    private val koin: Koin,
    private val activity: FragmentActivity,
) : FragmentFactory() {

    @Suppress("TooGenericExceptionCaught")
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment =
        try {
            Timber.d("KoinFragmentFactory:instantiate:$className")
            val clazz = loadFragmentClass(classLoader, className)
            koin.get(
                clazz = clazz.kotlin,
                qualifier = null,
                parameters = { parametersOf(activity) },
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
    noinline definition: FragmentDefinition<T>,
): KoinDefinition<T> =
    factory { (activity: FragmentActivity) ->
        definition(activity)
    }

typealias FragmentDefinition<T> = Scope.(FragmentActivity) -> T
