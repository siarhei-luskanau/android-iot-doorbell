package siarhei.luskanau.iot.doorbell.dagger.di.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import javax.inject.Inject
import javax.inject.Provider
import siarhei.luskanau.iot.doorbell.dagger.di.common.PerActivity
import timber.log.Timber

@PerActivity
class DaggerFragmentFactory @Inject constructor(
    private val providers: Map<Class<out Fragment>, @JvmSuppressWildcards Provider<Fragment>>
) : FragmentFactory() {

    override fun instantiate(
        classLoader: ClassLoader,
        className: String
    ): Fragment {
        Timber.d("DaggerFragmentFactory.instantiate: $className")
        try {
            val fragmentClass = loadFragmentClass(classLoader, className)
            val fragment: Fragment = providers[fragmentClass]?.get()
                ?: super.instantiate(classLoader, className)
            return fragment
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}
