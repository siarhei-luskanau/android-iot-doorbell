package siarhei.luskanau.iot.doorbell.viewmodel

import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Flowable
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.include
import org.junit.Assert.assertEquals
import siarhei.luskanau.iot.doorbell.ArchTaskExecutorOverrideSpek
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository

object ImagesViewModelTest : Spek({

    include(ArchTaskExecutorOverrideSpek)

    val deviceId by memoized { "deviceId" }
    val imagesList: List<ImageData> by memoized { listOf(ImageData("imageId")) }

    val mockDoorbellRepository by memoized {
        mock<DoorbellRepository> {
            on { listenImagesList(deviceId) }.doReturn(Flowable.just(imagesList))
        }
    }

    val testSchedulerSet by memoized { SchedulerSet.test() }

    val imagesViewModel by memoized {
        ImagesViewModel(
                testSchedulerSet,
                mockDoorbellRepository
        )
    }

    describe("a imagesViewModel") {

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
                assertEquals(imagesList, captor.lastValue)
            }
        }

    }

})