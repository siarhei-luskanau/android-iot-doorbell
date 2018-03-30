package siarhei.luskanau.iot.doorbell.viewmodel

import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Completable
import junit.framework.Assert.assertEquals
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.include
import siarhei.luskanau.iot.doorbell.ArchTaskExecutorOverrideSpek
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository

object CameraImageRequestVewModelTest : Spek({

    include(ArchTaskExecutorOverrideSpek)

    val deviceId by memoized { "deviceId" }
    val cameraId by memoized { "cameraId" }

    val mockDoorbellRepository by memoized {
        mock<DoorbellRepository> {
            on { sendCameraImageRequest(deviceId, cameraId, true) }.doReturn(Completable.complete())
        }
    }

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