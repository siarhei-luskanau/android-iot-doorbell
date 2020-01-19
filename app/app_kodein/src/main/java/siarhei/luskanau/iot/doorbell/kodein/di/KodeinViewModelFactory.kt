package siarhei.luskanau.iot.doorbell.kodein.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.kodein.di.DKodein
import org.kodein.di.generic.instance
import timber.log.Timber

class KodeinViewModelFactory(
    private val injector: DKodein
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        Timber.d("KodeinViewModelFactory:create:$modelClass")
        return try {
            return injector.instance<ViewModel>(tag = modelClass.simpleName) as T
        } catch (kodeinThrowable: Throwable) {
            try {
                modelClass.newInstance()
            } catch (t: Throwable) {
                throw kodeinThrowable
            }
        }
    }
}
