package siarhei.luskanau.iot.doorbell.ui.doorbelllist

import androidx.paging.PagedList
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runBlockingTest
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import siarhei.luskanau.iot.doorbell.common.DoorbellsDataSource
import siarhei.luskanau.iot.doorbell.common.test.setArchTaskExecutor
import siarhei.luskanau.iot.doorbell.common.test.setTestCoroutineScope
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository

@ExperimentalCoroutinesApi
object DoorbellListViewModelTest : Spek({

    setArchTaskExecutor()
    setTestCoroutineScope()

    val appNavigation by memoized { mock<AppNavigation>() }
    val thisDeviceRepository by memoized { mock<ThisDeviceRepository>() }
    val doorbellsDataSource by memoized { mock<DoorbellsDataSource>() }

    val doorbellListViewModel by memoized {
        DoorbellListViewModel(
            appNavigation = appNavigation,
            thisDeviceRepository = thisDeviceRepository,
            doorbellsDataSource = doorbellsDataSource
        )
    }

    describe("a doorbellListViewModel") {

        xcontext("check requestData") {

            beforeEachTest {
                runBlockingTest {
                    doorbellListViewModel.getDoorbellListFlow().collect()
                }
            }

            it("should load initial doorbell list") {
                runBlockingTest {
                    verify(doorbellsDataSource, times(1)).loadInitial(any(), any())
                }
            }

            context("when doorbell list is empty") {

                val values = mutableListOf<DoorbellListState>()

                beforeEachTest {
                    runBlockingTest {
                        doorbellListViewModel.getDoorbellListFlow().collect { values.add(it) }
                    }
                }

                it("should call observer.onChanged with EmptyDoorbellListState") {
                    assertEquals(EmptyDoorbellListState, values.last())
                }
            }

            context("when doorbell list is filled") {

                val values = mutableListOf<DoorbellListState>()
                val pagedList by memoized { mock<PagedList<DoorbellData>>() }

                beforeEachTest {
                    runBlockingTest {
                        doorbellListViewModel.getDoorbellListFlow().collect { values.add(it) }
                    }
                }

                it("should call observer.onChanged with NormalDoorbellListState") {
                    assertEquals(
                        NormalDoorbellListState(pagedList),
                        values.last()
                    )
                }
            }
        }
    }
})
