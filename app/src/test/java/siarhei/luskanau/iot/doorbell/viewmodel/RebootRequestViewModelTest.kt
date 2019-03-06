package siarhei.luskanau.iot.doorbell.viewmodel

import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import siarhei.luskanau.iot.doorbell.AppConstants
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.repository.UptimeRepository
import siarhei.luskanau.iot.doorbell.setArchTaskExecutor

object RebootRequestViewModelTest : Spek({

    setArchTaskExecutor()

    val deviceId by memoized { "deviceId" }
    val rebootRequestTimeMillis: Long by memoized { 1L }
    val rebootRequestTimeString: String by memoized { AppConstants.DATE_FORMAT.format(rebootRequestTimeMillis) }

    val testSchedulerSet by memoized { SchedulerSet.test() }

    val mockUptimeRepository by memoized { mock<UptimeRepository>() }

    val rebootRequestViewModel by memoized { RebootRequestViewModel(testSchedulerSet, mockUptimeRepository) }

    describe("a rebootRequestViewModel") {

        context("check uptimeRebootRequest") {
            beforeEachTest {
                rebootRequestViewModel.rebootDevice(deviceId, rebootRequestTimeMillis)
            }
            it("should send image request") {
                runBlocking {
                    verify(mockUptimeRepository, times(1)).uptimeRebootRequest(eq(deviceId), eq(rebootRequestTimeMillis), eq(rebootRequestTimeString))
                }
            }
        }
    }
})