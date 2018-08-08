package siarhei.luskanau.iot.doorbell.viewmodel

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import androidx.lifecycle.Observer
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Flowable
import kotlin.test.assertEquals
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository

object CamerasViewModelTest : Spek({

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
                assertEquals(cameraList, captor.lastValue)
            }
        }

    }

})