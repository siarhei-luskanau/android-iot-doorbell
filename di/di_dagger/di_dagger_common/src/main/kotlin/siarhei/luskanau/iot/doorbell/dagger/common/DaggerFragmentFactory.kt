package siarhei.luskanau.iot.doorbell.dagger.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import javax.inject.Provider

class DaggerFragmentFactory(
    private val providers: MutableMap<Class<out Fragment>, Provider<Fragment>>
) : FragmentFactory() {

    override fun instantiate(
        classLoader: ClassLoader,
        className: String
    ): Fragment {
        val fragmentClass = loadFragmentClass(classLoader, className)
        val fragment = providers[fragmentClass]?.get()
        return requireNotNull(fragment)
    }
}
