package siarhei.luskanau.iot.doorbell.viewmodel

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.datasource.doorbells.DoorbellsDataSource
import javax.inject.Inject

class DoorbellsViewModel @Inject constructor(
    doorbellsDataSource: DoorbellsDataSource
) : BaseViewModel() {

    val doorbellsLiveData: LiveData<PagedList<DoorbellData>> =
            LivePagedListBuilder(
                    object : DataSource.Factory<String, DoorbellData>() {
                        override fun create() = doorbellsDataSource
                    },
                    20
            ).build()
}
