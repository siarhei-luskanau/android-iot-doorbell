package siarhei.luskanau.iot.doorbell.viewmodel

import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Completable
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.include
import org.junit.Assert.assertEquals
import siarhei.luskanau.iot.doorbell.AppConstants
import siarhei.luskanau.iot.doorbell.ArchTaskExecutorOverrideSpek
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.repository.UptimeRepository

object RebootRequestViewModelTest : Spek({

    include(ArchTaskExecutorOverrideSpek)

    val deviceId by memoized { "deviceId" }
    val rebootRequestTimeMillis: Long by memoized { 1L }
    val rebootRequestTimeString: String by memoized { AppConstants.DATE_FORMAT.format(rebootRequestTimeMillis) }

    val testSchedulerSet by memoized { SchedulerSet.test() }

    val mockUptimeRepository by memoized {
        mock<UptimeRepository> {
            on { uptimeRebootRequest(any(), any(), any()) }.doReturn(Completable.complete())
        }
    }

    val rebootRequestViewModel by memoized {
        RebootRequestViewModel(
                testSchedulerSet,
                mockUptimeRepository
        )
    }

    describe("a rebootRequestViewModel") {

        context("check uptimeRebootRequest") {
            val observer = mock<Observer<Long>>()
            beforeEachTest {
                rebootRequestViewModel.uptimeRebootRequestUpdateLiveData.observeForever(observer)
                rebootRequestViewModel.deviceIdRebootRequestTimeLiveData.value = Pair(deviceId, rebootRequestTimeMillis)
                testSchedulerSet.triggerActions()
            }
            it("should send image request") {
                verify(mockUptimeRepository, times(1)).uptimeRebootRequest(eq(deviceId), eq(rebootRequestTimeMillis), eq(rebootRequestTimeString))
            }
            it("should call observer.onChanged") {
                val captor = argumentCaptor<Long>()
                verify(observer, atLeastOnce()).onChanged(captor.capture())
                assertEquals(rebootRequestTimeMillis, captor.lastValue)
            }
        }

    }

})