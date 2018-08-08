package siarhei.luskanau.iot.doorbell.datasource.doorbells

import androidx.paging.ItemKeyedDataSource
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData

abstract class DoorbellsDataSource : ItemKeyedDataSource<String, DoorbellData>()