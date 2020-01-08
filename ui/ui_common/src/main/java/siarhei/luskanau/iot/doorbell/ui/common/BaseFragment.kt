package siarhei.luskanau.iot.doorbell.ui.common

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelStore

abstract class BaseFragment<T>(
    private val presenterProvider: (args: Bundle?, store: ViewModelStore) -> T
) : Fragment() {

    protected val presenter: T by lazy { presenterProvider(arguments, viewModelStore) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeDataSources()
    }

    protected open fun observeDataSources() {
    }
}
