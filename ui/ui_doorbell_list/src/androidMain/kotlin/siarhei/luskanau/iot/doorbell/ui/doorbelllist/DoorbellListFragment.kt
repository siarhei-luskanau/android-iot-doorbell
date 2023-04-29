package siarhei.luskanau.iot.doorbell.ui.doorbelllist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.ui.common.BaseFragment
import siarhei.luskanau.iot.doorbell.ui.common.ErrorItem
import siarhei.luskanau.iot.doorbell.ui.common.LoadingItem
import siarhei.luskanau.iot.doorbell.ui.common.R

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
                        onItemClickListener = { doorbellData ->
                            doorbellData?.let { presenter.onDoorbellClicked(it) }
                        },
                    )
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.checkPermissions()
    }
}

@Composable
@Suppress("FunctionNaming")
fun DoorbellListComposable(
    items: LazyPagingItems<DoorbellData>,
    onItemClickListener: (DoorbellData?) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(all = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(
            items = items,
            key = { doorbellData ->
                doorbellData.doorbellId
            },
        ) { doorbellData ->
            doorbellData?.let {
                DoorbellDataItem(
                    doorbellData = doorbellData,
                    onItemClickListener = onItemClickListener,
                )
            }
        }
        when (val loadState = items.loadState.append) {
            is LoadState.Error -> item { ErrorItem(loadState.error.message ?: "error") }
            LoadState.Loading -> item { LoadingItem() }
            is LoadState.NotLoading -> item {
                if (items.itemCount <= 0) {
                    Text(text = "no data")
                }
            }
        }
        when (val loadState = items.loadState.refresh) {
            is LoadState.Error -> item { ErrorItem(loadState.error.message ?: "error") }
            LoadState.Loading -> item { LoadingItem() }
            is LoadState.NotLoading -> Unit
        }
    }
}

@Composable
@Suppress("FunctionNaming")
fun DoorbellDataItem(
    doorbellData: DoorbellData,
    onItemClickListener: (DoorbellData?) -> Unit,
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
        color = MaterialTheme.colors.background,
        elevation = 2.dp,
        border = if (MaterialTheme.colors.isLight) {
            null
        } else {
            BorderStroke(1.dp, MaterialTheme.colors.surface)
        },
    ) {
        Row(
            modifier = Modifier
                .padding(all = 8.dp)
                .fillMaxWidth()
                .clickable { onItemClickListener.invoke(doorbellData) },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val text = doorbellData.name ?: doorbellData.doorbellId
            Icon(
                painter = painterResource(id = R.drawable.ic_device_hub),
                contentDescription = text,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.h5,
            )
        }
    }
}

@Composable
@Suppress("FunctionNaming")
@Preview
fun DoorbellDataItemPreview() {
    DoorbellDataItem(
        doorbellData = DoorbellData(
            doorbellId = "doorbellId",
            name = "name",
        ),
        onItemClickListener = {},
    )
}
