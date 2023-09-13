package siarhei.luskanau.iot.doorbell.ui.doorbelllist

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.ui.common.ErrorItem
import siarhei.luskanau.iot.doorbell.ui.common.LoadingItem
import siarhei.luskanau.iot.doorbell.ui.common.R

@Composable
fun DoorbellListComposable(
    items: LazyPagingItems<DoorbellData>,
    checkPermissions: () -> Unit,
    onItemClickListener: (DoorbellData?) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(all = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(
            count = items.itemCount,
            key = items.itemKey { it.doorbellId },
            contentType = items.itemContentType { null },
        ) { index ->
            val doorbellData = items[index]
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
    LaunchedEffect(true) {
        checkPermissions.invoke()
    }
}

@Composable
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
