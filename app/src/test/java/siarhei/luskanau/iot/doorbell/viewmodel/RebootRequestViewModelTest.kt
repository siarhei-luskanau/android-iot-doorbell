package siarhei.luskanau.iot.doorbell.viewmodel

import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import siarhei.luskanau.iot.doorbell.AppConstants
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.repository.UptimeRepository
import siarhei.luskanau.iot.doorbell.setArchTaskExecutor
import kotlin.test.assertEquals

object RebootRequestViewModelTest : Spek({

    setArchTaskExecutor()

    val deviceId by memoized { "deviceId" }
    val rebootRequestTimeMillis: Long by memoized { 1L }
    val rebootRequestTimeString: String by memoized { AppConstants.DATE_FORMAT.format(rebootRequestTimeMillis) }

    val testSchedulerSet by memoized { SchedulerSet.test() }

    val mockUptimeRepository by memoized { mock<UptimeRepository>() }

    val rebootRequestViewModel by memoized { RebootRequestViewModel(mockUptimeRepository) }

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