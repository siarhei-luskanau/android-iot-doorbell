package siarhei.luskanau.iot.doorbell.ui.common

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

abstract class BaseFragment<T>(
    private val presenterProvider: (fragment: Fragment) -> T
) : Fragment() {

    protected val presenter: T by lazy { presenterProvider(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeDataSources()
    }

    protected open fun observeDataSources() {
    }
}
