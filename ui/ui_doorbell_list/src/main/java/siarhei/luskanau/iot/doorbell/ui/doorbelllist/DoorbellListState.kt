package siarhei.luskanau.iot.doorbell.ui.doorbelllist

import androidx.paging.PagedList
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData

sealed class DoorbellListState

object EmptyDoorbellListState : DoorbellListState()

data class NormalDoorbellListState(val doorbellList: PagedList<DoorbellData>?) : DoorbellListState()

data class ErrorDoorbellListState(val error: Throwable) : DoorbellListState()
