package siarhei.luskanau.iot.doorbell.viewmodel

import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.setArchTaskExecutor
import kotlin.test.assertEquals

object CamerasViewModelTest : Spek({

    setArchTaskExecutor()

    val deviceId by memoized { "deviceId" }
    val cameraId by memoized { "cameraId" }
    val cameraList: List<CameraData> by memoized { listOf(CameraData(cameraId)) }

    val testSchedulerSet by memoized { SchedulerSet.test() }

    val mockDoorbellRepository by memoized {
        mock<DoorbellRepository> {
            on { getCamerasList(deviceId) }.doReturn(cameraList)
        }
    }

    val camerasViewModel by memoized {
        CamerasViewModel(
                testSchedulerSet,
                mockDoorbellRepository
        )
    }

    describe("a camerasViewModel") {

        context("check listenCamerasList") {
            val observer = mock<Observer<List<CameraData>>>()
            beforeEachTest {
                camerasViewModel.camerasLiveData.observeForever(observer)
                camerasViewModel.deviceIdLiveData.value = deviceId
                testSchedulerSet.triggerActions()
            }
            it("should load cameras") {
                verify(mockDoorbellRepository, times(1)).getCamerasList(eq(deviceId))
            }
            it("should call observer.onChanged") {
                val captor = argumentCaptor<List<CameraData>>()
                verify(observer, atLeastOnce()).onChanged(captor.capture())
                assertEquals(cameraList, captor.lastValue)
            }
        }
    }
})