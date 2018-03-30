package siarhei.luskanau.iot.doorbell.viewmodel

import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Flowable
import junit.framework.Assert.assertEquals
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.include
import siarhei.luskanau.iot.doorbell.ArchTaskExecutorOverrideSpek
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository

object DoorbellsViewModelTest : Spek({

    include(ArchTaskExecutorOverrideSpek)

    val deviceId by memoized { "deviceId" }
    val doorbellsList: List<DoorbellData> by memoized { listOf(DoorbellData(deviceId)) }

    val mockDoorbellRepository by memoized {
        mock<DoorbellRepository> {
            on { listenDoorbellsList() }.doReturn(Flowable.just(doorbellsList))
        }
    }

    val testSchedulerSet by memoized { SchedulerSet.test() }

    val doorbellsViewModel by memoized {
        DoorbellsViewModel(
                testSchedulerSet,
                mockDoorbellRepository
        )
    }

    describe("a doorbellsViewModel") {

        context("check doorbellsLiveData") {
            val observer = mock<Observer<List<DoorbellData>>>()
            beforeEachTest {
                doorbellsViewModel.doorbellsLiveData.observeForever(observer)
                testSchedulerSet.triggerActions()
            }
            it("should load cameras") {
                verify(mockDoorbellRepository, times(1)).listenDoorbellsList()
            }
            it("should call observer.onChanged") {
                val captor = argumentCaptor<List<DoorbellData>>()
                verify(observer, atLeastOnce()).onChanged(captor.capture())
                assertEquals(doorbellsList, captor.lastValue)
            }
        }

    }

})