package siarhei.luskanau.iot.doorbell.data

import android.content.Context
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import siarhei.luskanau.iot.doorbell.common.DeviceInfoProvider
import siarhei.luskanau.iot.doorbell.common.IpAddressProvider
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.repository.CameraRepository

object AndroidThisDeviceRepositoryTest : Spek({

    val doorbellId = "doorbellId"
    val deviceName = "deviceName"
    val deviceInfo = emptyMap<String, String>()
    val doorbellData = DoorbellData(doorbellId, deviceName, deviceInfo)
    val camerasList = listOf(CameraData("cameraId"))
    val ipAddressList = mapOf("InterfaceName" to "IpAddress")
    val context by memoized { mock(Context::class.java) }
    val deviceInfoProvider by memoized {
        mock(DeviceInfoProvider::class.java).apply {
            given(buildDoorbellId()).willReturn(doorbellId)
            given(buildDeviceName()).willReturn(deviceName)
            given(buildDeviceInfo()).willReturn(deviceInfo)
        }
    }
    val cameraRepository by memoized {
        mock(CameraRepository::class.java).apply {
            given(runBlocking { getCamerasList() }).willReturn(camerasList)
        }
    }
    val ipAddressProvider by memoized {
        mock(IpAddressProvider::class.java).apply {
            given(runBlocking { getIpAddressList() }).willReturn(ipAddressList)
        }
    }

    val androidThisDeviceRepository by memoized {
        AndroidThisDeviceRepository(
            context = context,
            deviceInfoProvider = deviceInfoProvider,
            cameraRepository = cameraRepository,
            ipAddressProvider = ipAddressProvider
        )
    }

    describe("a AndroidThisDeviceRepository") {

        context("check doorbellId") {
            it("should be the same doorbellId") {
                runBlocking {
                    assertEquals(doorbellId, androidThisDeviceRepository.doorbellId())
                }
            }
        }

        context("check doorbellData") {
            var resultDoorbellData: DoorbellData? = null
            beforeEachTest {
                runBlocking {
                    resultDoorbellData = androidThisDeviceRepository.doorbellData()
                }
            }
            it("should invoke buildDoorbellId") {
                verify(deviceInfoProvider, times(1)).buildDoorbellId()
            }
            it("should invoke buildDeviceName") {
                verify(deviceInfoProvider, times(1)).buildDeviceName()
            }
            it("should invoke buildDeviceInfo") {
                verify(deviceInfoProvider, times(1)).buildDeviceInfo()
            }
            it("should be the same DoorbellData") {
                assertEquals(doorbellData, resultDoorbellData)
            }
        }

        context("check getCamerasList") {
            var resultCamerasList: List<CameraData>? = null
            beforeEachTest {
                runBlocking {
                    resultCamerasList = androidThisDeviceRepository.getCamerasList()
                }
            }
            it("should invoke getCamerasList()") {
                runBlocking {
                    verify(cameraRepository, times(1)).getCamerasList()
                }
            }
            it("should be the same list") {
                assertEquals(camerasList, resultCamerasList)
            }
        }

        context("check getIpAddressList") {
            var resultIpAddressList: Map<String, String>? = null
            beforeEachTest {
                runBlocking {
                    resultIpAddressList = androidThisDeviceRepository.getIpAddressList()
                }
            }
            it("should invoke getIpAddressList()") {
                runBlocking {
                    verify(ipAddressProvider, times(1)).getIpAddressList()
                }
            }
            it("should be the same list") {
                assertEquals(ipAddressList, resultIpAddressList)
            }
        }
    }
})
