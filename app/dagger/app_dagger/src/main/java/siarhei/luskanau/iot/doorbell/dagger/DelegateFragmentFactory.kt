package siarhei.luskanau.iot.doorbell.dagger

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import java.lang.Exception

@Suppress("TooGenericExceptionCaught")
class DelegateFragmentFactory(
    private val providers: List<() -> FragmentFactory>
) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        for (provider in providers) {
            try {
                return provider.invoke().instantiate(classLoader, className)
            } catch (e: Exception) {
                // do nothing
            }
        }
        return super.instantiate(classLoader, className)
    }
}
