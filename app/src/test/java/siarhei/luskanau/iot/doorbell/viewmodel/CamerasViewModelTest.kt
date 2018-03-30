package siarhei.luskanau.iot.doorbell.viewmodel

import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Completable
import io.reactivex.Flowable
import junit.framework.Assert
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.include
import siarhei.luskanau.iot.doorbell.ArchTaskExecutorOverrideSpek
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository

object CamerasViewModelTest : Spek({

    include(ArchTaskExecutorOverrideSpek)

    val deviceId by memoized { "deviceId" }
    val cameraId by memoized { "cameraId" }
    val cameraList: List<CameraData> by memoized { listOf(CameraData(cameraId)) }

    val testSchedulerSet by memoized { SchedulerSet.test() }

    val mockDoorbellRepository by memoized {
        mock<DoorbellRepository> {
            on { listenCamerasList(deviceId) }.doReturn(Flowable.just(cameraList))
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
                verify(mockDoorbellRepository, times(1)).listenCamerasList(eq(deviceId))
            }
            it("should call observer.onChanged") {
                val captor = argumentCaptor<List<CameraData>>()
                verify(observer, atLeastOnce()).onChanged(captor.capture())
                Assert.assertEquals(cameraList, captor.lastValue)
            }
        }

    }

})