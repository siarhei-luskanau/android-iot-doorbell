package siarhei.luskanau.iot.doorbell.dagger.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import javax.inject.Provider
import timber.log.Timber

class DaggerFragmentFactory(
    private val providers: MutableMap<Class<out Fragment>, Provider<Fragment>>
) : FragmentFactory() {

    override fun instantiate(
        classLoader: ClassLoader,
        className: String
    ): Fragment =
        loadFragmentClass(classLoader, className).let { fragmentClass ->
            requireNotNull(providers[fragmentClass]).get()
        }.also {
            Timber.d("DaggerFragmentFactory.instantiate: $className")
        }
}
