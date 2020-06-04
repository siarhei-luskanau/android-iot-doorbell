package siarhei.luskanau.iot.doorbell.ui.common

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseFragment<T>(
    @LayoutRes layoutId: Int,
    private val presenterProvider: (fragment: Fragment) -> T
) : Fragment(layoutId) {

    protected val presenter: T by lazy { presenterProvider(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeDataSources()
    }

    protected open fun observeDataSources() {
    }
}
