package siarhei.luskanau.iot.doorbell.kodein.di

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.kodein.di.DirectDI
import org.kodein.di.instance
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import timber.log.Timber

class KodeinViewModelFactory(
    private val injector: DirectDI,
    private val appNavigation: AppNavigation,
    private val fragment: Fragment,
    private val args: Bundle?
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        runCatching {
            Timber.d("KodeinViewModelFactory:create:$modelClass")
            val viewModel: ViewModel = injector.instance(
                tag = modelClass.simpleName,
                arg = ViewModelFactoryArgs(
                    appNavigation = appNavigation,
                    fragment = fragment,
                    args = args,
                )
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
