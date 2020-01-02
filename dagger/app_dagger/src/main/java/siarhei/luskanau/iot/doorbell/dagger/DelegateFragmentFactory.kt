package siarhei.luskanau.iot.doorbell.dagger

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import javax.inject.Provider
import timber.log.Timber

class DelegateFragmentFactory(
    private val providers: List<Provider<FragmentFactory>>
) : FragmentFactory() {

    override fun instantiate(
        classLoader: ClassLoader,
        className: String
    ): Fragment {
        Timber.d("DelegateFragmentFactory.instantiate: $className")

        var fragment: Fragment? = null

        for (provider in providers) {
            val instantiateResult = kotlin.runCatching {
                fragment = provider.get().instantiate(classLoader, className)
            }
            if (instantiateResult.isSuccess) {
                break
            }
        }

        return fragment ?: super.instantiate(classLoader, className)
    }
}
