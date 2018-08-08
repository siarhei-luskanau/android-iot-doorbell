package siarhei.luskanau.iot.doorbell.viewmodel

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.datasource.doorbells.DoorbellsDataSource

object DoorbellsViewModelTest : Spek({

    beforeGroup {
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) {
                runnable.run()
            }

            override fun postToMainThread(runnable: Runnable) {
                runnable.run()
            }

            override fun isMainThread(): Boolean = true
        })
    }

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