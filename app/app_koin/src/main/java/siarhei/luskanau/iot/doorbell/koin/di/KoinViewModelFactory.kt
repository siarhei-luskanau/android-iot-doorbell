package siarhei.luskanau.iot.doorbell.koin.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.koin.core.Koin
import timber.log.Timber

class KoinViewModelFactory(
    private val koin: Koin
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        Timber.d("KoinViewModelFactory:create:$modelClass")
        return try {
            koin.get(
                clazz = modelClass.kotlin,
                qualifier = null,
                parameters = null
            )
        } catch (kodeinThrowable: Throwable) {
            try {
                modelClass.newInstance()
            } catch (t: Throwable) {
                throw kodeinThrowable
            }
        }
    }
}
