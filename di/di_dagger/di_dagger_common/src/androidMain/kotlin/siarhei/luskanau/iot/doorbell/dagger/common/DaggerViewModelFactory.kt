package siarhei.luskanau.iot.doorbell.dagger.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import timber.log.Timber

class DaggerViewModelFactory @Inject constructor(
    private val providers: MutableMap<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        Timber.d("DaggerViewModelFactory.create: $modelClass")
        return requireNotNull(getProvider(modelClass).get()) {
            "Provider for $modelClass returned null"
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : ViewModel> getProvider(modelClass: Class<T>): Provider<T> = try {
        requireNotNull(providers[modelClass] as Provider<T>) {
            "No ViewModel provider is bound for class $modelClass"
        }
    } catch (cce: ClassCastException) {
        error("Wrong provider type registered for ViewModel type $modelClass , $cce")
    }
}
