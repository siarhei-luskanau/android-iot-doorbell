package siarhei.luskanau.iot.doorbell.ui.doorbelllist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.paging.compose.collectAsLazyPagingItems
import siarhei.luskanau.iot.doorbell.ui.common.BaseFragment

class DoorbellListFragment(
    presenterProvider: (fragment: Fragment) -> DoorbellListPresenter,
) : BaseFragment<DoorbellListPresenter>(presenterProvider) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View =
        ComposeView(inflater.context).apply {
            setContent {
                MaterialTheme {
                    DoorbellListComposable(
                        presenter.doorbellListFlow.collectAsLazyPagingItems(),
                        checkPermissions = { presenter.checkPermissions() },
                        onItemClickListener = { doorbellData ->
                            doorbellData?.let { presenter.onDoorbellClicked(it) }
                        },
                    )
                }
            }
        }
}
