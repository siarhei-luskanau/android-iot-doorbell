package siarhei.luskanau.iot.doorbell.viewmodel

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Completable
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import siarhei.luskanau.iot.doorbell.AppConstants
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.repository.UptimeRepository
import kotlin.test.assertEquals

object RebootRequestViewModelTest : Spek({

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