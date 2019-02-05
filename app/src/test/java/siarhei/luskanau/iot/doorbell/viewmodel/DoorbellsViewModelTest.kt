package siarhei.luskanau.iot.doorbell.viewmodel

import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.datasource.doorbells.DoorbellsDataSource
import siarhei.luskanau.iot.doorbell.setArchTaskExecutor

object DoorbellsViewModelTest : Spek({

    setArchTaskExecutor()

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