package siarhei.luskanau.iot.doorbell.ui.doorbelllist

import androidx.fragment.app.Fragment
import siarhei.luskanau.iot.doorbell.ui.common.BaseFragment

class DoorbellListFragment(
    presenterProvider: (fragment: Fragment) -> DoorbellListPresenter,
) : BaseFragment<DoorbellListPresenter>(presenterProvider) {

//            DoorbellListComposable(
//                presenter.doorbellListFlow.collectAsLazyPagingItems(),
//                checkPermissions = { presenter.checkPermissions() },
//                onItemClickListener = { doorbellData ->
//                    doorbellData?.let { presenter.onDoorbellClicked(it) }
//                },
//            )
}
