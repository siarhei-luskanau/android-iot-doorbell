package siarhei.luskanau.iot.doorbell.ui.doorbelllist

import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.atLeast
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import siarhei.luskanau.iot.doorbell.data.TestSchedulerSet
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.repository.CameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import siarhei.luskanau.iot.doorbell.doomain.DoorbellsDataSource
import siarhei.luskanau.iot.doorbell.ui.setArchTaskExecutor

@ExperimentalCoroutinesApi
object DoorbellListViewModelTest : Spek({

    setArchTaskExecutor()

    val schedulerSet by memoized { TestSchedulerSet() }
    val doorbellRepository by memoized { mock<DoorbellRepository>() }
    val thisDeviceRepository by memoized { mock<ThisDeviceRepository>() }
    val cameraRepository by memoized { mock<CameraRepository>() }
    val doorbellsDataSource by memoized { mock<DoorbellsDataSource>() }

    val doorbellListViewModel by memoized {
        DoorbellListViewModel(
            schedulerSet = schedulerSet,
            doorbellRepository = doorbellRepository,
            thisDeviceRepository = thisDeviceRepository,
            cameraRepository = cameraRepository,
            doorbellsDataSource = doorbellsDataSource
        )
    }

    describe("a doorbellListViewModel") {

        context("check requestData") {

            beforeEachTest {
                val observer = mock<Observer<DoorbellListState>>()
                doorbellListViewModel.doorbellListStateData.observeForever(observer)
                doorbellListViewModel.requestData()
            }

            it("should load camera list") {
                runBlocking {
                    verify(cameraRepository, times(1)).getCamerasList()
                }
            }

            it("should load initial doorbell list") {
                runBlocking {
                    verify(doorbellsDataSource, times(1)).loadInitial(any(), any())
                }
            }

            context("when camera list is empty and doorbell list is empty") {

                val observer = mock<Observer<DoorbellListState>>()
                val expectedCameraList = emptyList<CameraData>()

                beforeEachTest {
                    runBlocking {
                        given(cameraRepository.getCamerasList()).willReturn(expectedCameraList)
                    }
                    doorbellListViewModel.doorbellListStateData.observeForever(observer)
                    doorbellListViewModel.requestData()
                }

                it("should call observer.onChanged with EmptyDoorbellListState") {
                    val captor = argumentCaptor<DoorbellListState>()
                    verify(observer, atLeast(2)).onChanged(captor.capture())
                    assertEquals(EmptyDoorbellListState(expectedCameraList), captor.lastValue)
                }
            }

            context("when camera list is filled and doorbell list is empty") {

                val observer = mock<Observer<DoorbellListState>>()
                val expectedCameraList = listOf(
                    CameraData(cameraId = "cameraId_1"),
                    CameraData(cameraId = "cameraId_2")
                )

                beforeEachTest {
                    runBlocking {
                        given(cameraRepository.getCamerasList()).willReturn(expectedCameraList)
                    }
                    doorbellListViewModel.doorbellListStateData.observeForever(observer)
                    doorbellListViewModel.requestData()
                }

                it("should call observer.onChanged with EmptyDoorbellListState") {
                    val captor = argumentCaptor<DoorbellListState>()
                    verify(observer, atLeast(2)).onChanged(captor.capture())
                    assertEquals(EmptyDoorbellListState(expectedCameraList), captor.lastValue)
                }
            }

            xcontext("when camera list is empty and doorbell list is filled") {

                val observer = mock<Observer<DoorbellListState>>()
                val expectedCameraList = emptyList<CameraData>()
                val pagedList by memoized { mock<PagedList<DoorbellData>>() }

                beforeEachTest {
                    runBlocking {
                        given(cameraRepository.getCamerasList()).willReturn(expectedCameraList)
                    }
                    doorbellListViewModel.doorbellListStateData.observeForever(observer)
                    doorbellListViewModel.requestData()
                }

                it("should call observer.onChanged with NormalDoorbellListState") {
                    val captor = argumentCaptor<DoorbellListState>()
                    verify(observer, atLeastOnce()).onChanged(captor.capture())
                    assertEquals(
                        NormalDoorbellListState(expectedCameraList, pagedList),
                        captor.lastValue
                    )
                }
            }
        }
    }
})
