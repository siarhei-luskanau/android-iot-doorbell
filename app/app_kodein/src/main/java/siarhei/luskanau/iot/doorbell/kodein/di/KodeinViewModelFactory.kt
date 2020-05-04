package siarhei.luskanau.iot.doorbell.kodein.di

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.kodein.di.DKodein
import org.kodein.di.generic.M
import org.kodein.di.generic.instance
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import timber.log.Timber

class KodeinViewModelFactory(
    private val injector: DKodein,
    private val appNavigation: AppNavigation,
    private val args: Bundle?
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        runCatching {
            Timber.d("KodeinViewModelFactory:create:$modelClass")
            val viewModel: ViewModel = injector.instance(
                tag = modelClass.simpleName,
                arg = M(appNavigation, args)
            )
            viewModel as T
        }
            .getOrElse { kodeinThrowable: Throwable ->
                runCatching {
                    modelClass.newInstance()
                }
                    .getOrNull() ?: throw kodeinThrowable
            }
}
