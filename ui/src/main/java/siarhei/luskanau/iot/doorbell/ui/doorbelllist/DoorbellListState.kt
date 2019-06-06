package siarhei.luskanau.iot.doorbell.ui.doorbelllist

import androidx.paging.PagedList
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData

sealed class DoorbellListState

data class EmptyDoorbellListState(val cameraList: List<CameraData>) : DoorbellListState()

data class NormalDoorbellListState(
    val cameraList: List<CameraData>,
    val doorbellList: PagedList<DoorbellData>
) : DoorbellListState()

data class ErrorDoorbellListState(val error: Throwable) : DoorbellListState()
