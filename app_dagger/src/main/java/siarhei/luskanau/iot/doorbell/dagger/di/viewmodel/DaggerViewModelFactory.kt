package siarhei.luskanau.iot.doorbell.dagger.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class DaggerViewModelFactory @Inject constructor(
    private val providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        Timber.d("DaggerViewModelFactory.create: $modelClass")
        return requireNotNull(getProvider(modelClass).get()) {
            "Provider for $modelClass returned null"
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : ViewModel> getProvider(modelClass: Class<T>): Provider<T> =
            try {
                requireNotNull(providers[modelClass] as Provider<T>) {
                    "No ViewModel provider is bound for class $modelClass"
                }
            } catch (cce: ClassCastException) {
                error("Wrong provider type registered for ViewModel type $modelClass")
            }
}