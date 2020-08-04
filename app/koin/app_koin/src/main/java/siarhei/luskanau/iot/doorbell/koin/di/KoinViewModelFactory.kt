package siarhei.luskanau.iot.doorbell.koin.di

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.koin.core.Koin
import org.koin.core.parameter.parametersOf
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import timber.log.Timber

class KoinViewModelFactory(
    private val koin: Koin,
    private val appNavigation: AppNavigation,
    private val args: Bundle?
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        runCatching<T> {
            Timber.d("KoinViewModelFactory:create:$modelClass")
            koin.get(
                clazz = modelClass.kotlin,
                qualifier = null,
                parameters = { parametersOf(appNavigation, args) }
            )
        }
            .getOrElse { koinThrowable: Throwable ->
                runCatching {
                    modelClass.newInstance()
                }
                    .getOrNull() ?: throw koinThrowable
            }
}