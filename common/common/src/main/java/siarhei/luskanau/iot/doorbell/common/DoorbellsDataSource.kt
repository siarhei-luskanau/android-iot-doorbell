package siarhei.luskanau.iot.doorbell.common

import androidx.paging.ItemKeyedDataSource
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData

abstract class DoorbellsDataSource : ItemKeyedDataSource<String, DoorbellData>()
