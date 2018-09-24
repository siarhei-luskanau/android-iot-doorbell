package siarhei.luskanau.iot.doorbell.viewmodel

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import kotlin.test.assertEquals

object CameraImageRequestVewModelTest : Spek({

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
    val cameraId by memoized { "cameraId" }

    val mockDoorbellRepository by memoized { mock<DoorbellRepository>() }

    val testSchedulerSet by memoized { SchedulerSet.test() }

    val cameraImageRequestVewModel by memoized {
        CameraImageRequestVewModel(
                testSchedulerSet,
                mockDoorbellRepository
        )
    }

    describe("a cameraImageRequestVewModel") {

        context("check sendCameraImageRequest") {
            val observer = mock<Observer<String>>()
            beforeEachTest {
                cameraImageRequestVewModel.cameraImageRequestLiveData.observeForever(observer)
                cameraImageRequestVewModel.deviceIdCameraIdLiveData.value = Pair(deviceId, cameraId)
                testSchedulerSet.triggerActions()
            }
            it("should send image request") {
                verify(mockDoorbellRepository, times(1)).sendCameraImageRequest(eq(deviceId), eq(cameraId), eq(true))
            }
            it("should call observer.onChanged") {
                val captor = argumentCaptor<String>()
                verify(observer, atLeastOnce()).onChanged(captor.capture())
                assertEquals(cameraId, captor.lastValue)
            }
        }

    }

})