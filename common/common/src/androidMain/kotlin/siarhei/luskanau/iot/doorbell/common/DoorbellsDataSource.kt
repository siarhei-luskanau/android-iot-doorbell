package siarhei.luskanau.iot.doorbell.common

import androidx.paging.PagingSource
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData

@Suppress("UnnecessaryAbstractClass")
abstract class DoorbellsDataSource : PagingSource<String, DoorbellData>()
