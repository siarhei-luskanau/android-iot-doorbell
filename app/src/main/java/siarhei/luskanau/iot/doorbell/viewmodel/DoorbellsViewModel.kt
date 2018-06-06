package siarhei.luskanau.iot.doorbell.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.datasource.doorbells.DoorbellsDataSource
import javax.inject.Inject

class DoorbellsViewModel @Inject constructor(
        doorbellsDataSource: DoorbellsDataSource
) : ViewModel() {

    val doorbellsLiveData: LiveData<PagedList<DoorbellData>> =
            LivePagedListBuilder(
                    object : DataSource.Factory<String, DoorbellData>() {
                        override fun create() = doorbellsDataSource
                    },
                    20
            ).build()

}
