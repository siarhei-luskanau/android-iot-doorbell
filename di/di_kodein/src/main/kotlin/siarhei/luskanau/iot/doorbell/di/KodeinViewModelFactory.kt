package siarhei.luskanau.iot.doorbell.di

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.kodein.di.DirectDI
import org.kodein.di.instance
import timber.log.Timber

class KodeinViewModelFactory(
    private val injector: DirectDI,
    private val activity: FragmentActivity,
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
                    activity = activity,
                    fragment = fragment,
                    args = args
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
