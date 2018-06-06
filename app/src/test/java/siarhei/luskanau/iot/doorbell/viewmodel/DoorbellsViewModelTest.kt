package siarhei.luskanau.iot.doorbell.viewmodel

import android.arch.lifecycle.Observer
import android.arch.paging.PagedList
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.include
import siarhei.luskanau.iot.doorbell.ArchTaskExecutorOverrideSpek
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.datasource.doorbells.DoorbellsDataSource

object DoorbellsViewModelTest : Spek({

    include(ArchTaskExecutorOverrideSpek)

    val mockDoorbellsDataSource by memoized { mock<DoorbellsDataSource>() }

    val testSchedulerSet by memoized { SchedulerSet.test() }

    val doorbellsViewModel by memoized {
        DoorbellsViewModel(
                mockDoorbellsDataSource
        )
    }

    describe("a doorbellsViewModel") {

        context("check doorbellsLiveData") {
            val observer = mock<Observer<PagedList<DoorbellData>>>()
            beforeEachTest {
                doorbellsViewModel.doorbellsLiveData.observeForever(observer)
                testSchedulerSet.triggerActions()
            }
            it("should load cameras") {
                verify(mockDoorbellsDataSource, times(1)).loadInitial(any(), any())
            }
        }

    }

})