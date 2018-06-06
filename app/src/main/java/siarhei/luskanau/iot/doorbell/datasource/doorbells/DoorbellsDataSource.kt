package siarhei.luskanau.iot.doorbell.datasource.doorbells

import android.arch.paging.ItemKeyedDataSource
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData

abstract class DoorbellsDataSource : ItemKeyedDataSource<String, DoorbellData>()