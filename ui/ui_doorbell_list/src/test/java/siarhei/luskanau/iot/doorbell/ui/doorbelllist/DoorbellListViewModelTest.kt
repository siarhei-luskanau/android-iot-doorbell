package siarhei.luskanau.iot.doorbell.ui.doorbelllist

import androidx.paging.PagedList
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runBlockingTest
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import siarhei.luskanau.iot.doorbell.common.DoorbellsDataSource
import siarhei.luskanau.iot.doorbell.common.test.setArchTaskExecutor
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.repository.CameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository

@ExperimentalCoroutinesApi
object DoorbellListViewModelTest : Spek({

    setArchTaskExecutor()

    val doorbellRepository by memoized { mock<DoorbellRepository>() }
    val thisDeviceRepository by memoized { mock<ThisDeviceRepository>() }
    val cameraRepository by memoized { mock<CameraRepository>() }
    val doorbellsDataSource by memoized { mock<DoorbellsDataSource>() }

    val doorbellListViewModel by memoized {
        DoorbellListViewModel(
            doorbellRepository = doorbellRepository,
            thisDeviceRepository = thisDeviceRepository,
            cameraRepository = cameraRepository,
            doorbellsDataSource = doorbellsDataSource
        )
    }

    describe("a doorbellListViewModel") {

        context("check requestData") {

            beforeEachTest {
                runBlockingTest {
                    doorbellListViewModel.doorbellListStateFlow.collect()
                }
            }

            it("should load camera list") {
                runBlockingTest {
                    verify(cameraRepository, times(1)).getCamerasList()
                }
            }

            it("should load initial doorbell list") {
                runBlockingTest {
                    verify(doorbellsDataSource, times(1)).loadInitial(any(), any())
                }
            }

            context("when camera list is empty and doorbell list is empty") {

                val values = mutableListOf<DoorbellListState>()
                val expectedCameraList = emptyList<CameraData>()

                beforeEachTest {
                    runBlockingTest {
                        given(cameraRepository.getCamerasList()).willReturn(expectedCameraList)
                        doorbellListViewModel.doorbellListStateFlow.collect { values.add(it) }
                    }
                }

                it("should call observer.onChanged with EmptyDoorbellListState") {
                    assertEquals(EmptyDoorbellListState(expectedCameraList), values.last())
                }
            }

            context("when camera list is filled and doorbell list is empty") {

                val values = mutableListOf<DoorbellListState>()
                val expectedCameraList = listOf(
                    CameraData(cameraId = "cameraId_1"),
                    CameraData(cameraId = "cameraId_2")
                )

                beforeEachTest {
                    runBlockingTest {
                        given(cameraRepository.getCamerasList()).willReturn(expectedCameraList)
                        doorbellListViewModel.doorbellListStateFlow.collect { values.add(it) }
                    }
                }

                it("should call observer.onChanged with EmptyDoorbellListState") {
                    assertEquals(EmptyDoorbellListState(expectedCameraList), values.last())
                }
            }

            xcontext("when camera list is empty and doorbell list is filled") {

                val values = mutableListOf<DoorbellListState>()
                val expectedCameraList = emptyList<CameraData>()
                val pagedList by memoized { mock<PagedList<DoorbellData>>() }

                beforeEachTest {
                    runBlockingTest {
                        given(cameraRepository.getCamerasList()).willReturn(expectedCameraList)
                        doorbellListViewModel.doorbellListStateFlow.collect { values.add(it) }
                    }
                }

                it("should call observer.onChanged with NormalDoorbellListState") {
                    assertEquals(
                        NormalDoorbellListState(expectedCameraList, pagedList),
                        values.last()
                    )
                }
            }
        }
    }
})
