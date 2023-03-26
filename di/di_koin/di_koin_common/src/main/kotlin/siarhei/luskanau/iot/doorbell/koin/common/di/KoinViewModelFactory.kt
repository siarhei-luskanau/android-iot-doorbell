package siarhei.luskanau.iot.doorbell.koin.common.di

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import timber.log.Timber

class KoinViewModelFactory(
    private val scope: Scope,
    private val activity: FragmentActivity,
    private val fragment: Fragment,
    private val args: Bundle?
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("TooGenericExceptionCaught")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        try {
            Timber.d("KoinViewModelFactory:create:$modelClass")
            scope.get(
                clazz = modelClass.kotlin,
                qualifier = null,
                parameters = { parametersOf(activity, fragment, args) }
            )
        } catch (koinThrowable: Throwable) {
            try {
                modelClass.newInstance()
            } catch (_: Throwable) {
                throw koinThrowable
            }
        }
}

inline fun <reified T : ViewModel> Module.viewModel(
    noinline definition: ViewModelDefinition<T>
): KoinDefinition<T> =
    factory { (activity: FragmentActivity, fragment: Fragment, args: Bundle?) ->
        definition(activity, fragment, args)
    }

typealias ViewModelDefinition<T> = Scope.(FragmentActivity, Fragment, Bundle?) -> T
