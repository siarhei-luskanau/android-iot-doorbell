package siarhei.luskanau.iot.doorbell.viewmodel

import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.setArchTaskExecutor

object CameraImageRequestVewModelTest : Spek({

    setArchTaskExecutor()

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
            beforeEachTest {
                cameraImageRequestVewModel.requestCameraImage(deviceId, cameraId)
                testSchedulerSet.triggerActions()
            }
            it("should send image request") {
                verify(mockDoorbellRepository, times(1)).sendCameraImageRequest(eq(deviceId), eq(cameraId), eq(true))
            }
        }
    }
})