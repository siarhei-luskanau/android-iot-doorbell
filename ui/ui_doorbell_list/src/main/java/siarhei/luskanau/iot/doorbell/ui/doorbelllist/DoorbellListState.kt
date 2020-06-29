package siarhei.luskanau.iot.doorbell.ui.doorbelllist

import androidx.paging.PagingData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData

sealed class DoorbellListState

object EmptyDoorbellListState : DoorbellListState()

data class NormalDoorbellListState(val pagingData: PagingData<DoorbellData>) : DoorbellListState()

data class ErrorDoorbellListState(val error: Throwable) : DoorbellListState()
