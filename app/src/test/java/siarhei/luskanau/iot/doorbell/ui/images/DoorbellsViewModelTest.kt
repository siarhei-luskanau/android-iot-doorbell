package siarhei.luskanau.iot.doorbell.ui.images

import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import junit.framework.Assert.assertEquals
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.include
import siarhei.luskanau.iot.doorbell.ArchTaskExecutorOverrideSpek
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import siarhei.luskanau.iot.doorbell.ui.doorbells.DoorbellsViewModel

object DoorbellsViewModelTest : Spek({

    include(ArchTaskExecutorOverrideSpek)

    val deviceId = "deviceId"
    val cameraId = "cameraId"
    val cameraList: List<CameraData> = listOf(CameraData(cameraId))
    val doorbellsList: List<DoorbellData> = listOf(DoorbellData(deviceId))

    val mockDoorbellRepository by memoized {
        mock<DoorbellRepository> {
            on { listenDoorbellsList() }.doReturn(Flowable.just(doorbellsList))
            on { sendCameraImageRequest(deviceId, cameraId, true) }.doReturn(Completable.complete())
        }
    }

    val mockThisDeviceRepository by memoized {
        mock<ThisDeviceRepository> {
            on { doorbellId() }.doReturn(deviceId)
            on { getCamerasList() }.doReturn(Single.just(cameraList))
        }
    }

    val testSchedulerSet = SchedulerSet.test()

    val doorbellsViewModel by memoized {
        DoorbellsViewModel(
                testSchedulerSet,
                mockDoorbellRepository,
                mockThisDeviceRepository
        )
    }

    describe("a doorbellsViewModel") {

        context("check getCamerasList") {
            val observer = mock<Observer<List<CameraData>>>()
            beforeEachTest {
                doorbellsViewModel.camerasLiveData.observeForever(observer)
                testSchedulerSet.triggerActions()
            }
            it("should load cameras") {
                verify(mockThisDeviceRepository, times(1)).getCamerasList()
            }
            it("should call observer.onChanged") {
                val captor = argumentCaptor<List<CameraData>>()
                verify(observer, atLeastOnce()).onChanged(captor.capture())
                assertEquals(captor.lastValue, cameraList)
            }
        }

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
                assertEquals(captor.lastValue, doorbellsList)
            }
        }

        context("check sendCameraImageRequest") {
            val observer = mock<Observer<String>>()
            beforeEachTest {
                doorbellsViewModel.cameraImageRequestLiveData.observeForever(observer)
                doorbellsViewModel.cameraIdLiveData.value = cameraId
                testSchedulerSet.triggerActions()
            }
            it("should getting doorbellId of this device") {
                verify(mockThisDeviceRepository, times(1)).doorbellId()
            }
            it("should send image request") {
                verify(mockDoorbellRepository, times(1)).sendCameraImageRequest(eq(deviceId), eq(cameraId), eq(true))
            }
            it("should call observer.onChanged") {
                val captor = argumentCaptor<String>()
                verify(observer, atLeastOnce()).onChanged(captor.capture())
                assertEquals(captor.lastValue, cameraId)
            }
        }

    }

})