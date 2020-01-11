package siarhei.luskanau.iot.doorbell.koin.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import org.koin.core.context.GlobalContext
import org.koin.core.parameter.parametersOf
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import timber.log.Timber

class KoinFragmentFactory(
    private val appNavigation: AppNavigation
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment =
        try {
            Timber.d("KoinFragmentFactory:instantiate:$className")
            val clazz = loadFragmentClass(classLoader, className).kotlin
            GlobalContext.get().koin.get(
                clazz = clazz,
                qualifier = null,
                parameters = { parametersOf(appNavigation) }
            )
        } catch (koinThrowable: Throwable) {
            try {
                super.instantiate(classLoader, className)
            } catch (t: Throwable) {
                throw koinThrowable
            }
        }
}