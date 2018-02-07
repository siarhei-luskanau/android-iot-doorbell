package siarhei.luskanau.iot.doorbell.ui.images

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
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository

object ImagesViewModelTest : Spek({

    include(ArchTaskExecutorOverrideSpek)

    val deviceId = "deviceId"
    val cameraId = "cameraId"
    val cameraList: List<CameraData> = listOf(CameraData(cameraId))
    val imagesList: List<ImageData> = listOf(ImageData("imageId"))

    val mockDoorbellRepository by memoized {
        mock<DoorbellRepository> {
            on { listenCamerasList(deviceId) }.doReturn(Flowable.just(cameraList))
            on { listenImagesList(deviceId) }.doReturn(Flowable.just(imagesList))
            on { sendCameraImageRequest(deviceId, cameraId, true) }.doReturn(Completable.complete())
        }
    }

    val testSchedulerSet = SchedulerSet.test()

    val imagesViewModel by memoized {
        ImagesViewModel(
                testSchedulerSet,
                mockDoorbellRepository
        )
    }

    describe("a imagesViewModel") {

        context("check listenCamerasList") {
            val observer = mock<Observer<List<CameraData>>>()
            beforeEachTest {
                imagesViewModel.camerasLiveData.observeForever(observer)
                imagesViewModel.deviceIdLiveData.value = deviceId
                testSchedulerSet.triggerActions()
            }
            it("should load cameras") {
                verify(mockDoorbellRepository, times(1)).listenCamerasList(eq(deviceId))
            }
            it("should call observer.onChanged") {
                val captor = argumentCaptor<List<CameraData>>()
                verify(observer, atLeastOnce()).onChanged(captor.capture())
                Assert.assertEquals(captor.lastValue, cameraList)
            }
        }

        context("check listenImagesList") {
            val observer = mock<Observer<List<ImageData>>>()
            beforeEachTest {
                imagesViewModel.imagesLiveData.observeForever(observer)
                imagesViewModel.deviceIdLiveData.value = deviceId
                testSchedulerSet.triggerActions()
            }
            it("should load images") {
                verify(mockDoorbellRepository, times(1)).listenImagesList(eq(deviceId))
            }
            it("should call observer.onChanged") {
                val captor = argumentCaptor<List<ImageData>>()
                verify(observer, atLeastOnce()).onChanged(captor.capture())
                Assert.assertEquals(captor.lastValue, imagesList)
            }
        }

        context("check sendCameraImageRequest") {
            val observer = mock<Observer<String>>()
            beforeEachTest {
                imagesViewModel.cameraImageRequestLiveData.observeForever(observer)
                imagesViewModel.deviceIdLiveData.value = deviceId
                imagesViewModel.cameraIdLiveData.value = cameraId
                testSchedulerSet.triggerActions()
            }
            it("should send image request") {
                verify(mockDoorbellRepository, times(1)).sendCameraImageRequest(eq(deviceId), eq(cameraId), eq(true))
            }
            it("should call observer.onChanged") {
                val captor = argumentCaptor<String>()
                verify(observer, atLeastOnce()).onChanged(captor.capture())
                Assert.assertEquals(captor.lastValue, cameraId)
            }
        }

    }

})