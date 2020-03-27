package siarhei.luskanau.iot.doorbell.data

import android.content.Context
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import java.io.Serializable
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import siarhei.luskanau.iot.doorbell.common.DeviceInfoProvider
import siarhei.luskanau.iot.doorbell.common.IpAddressProvider
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.repository.CameraRepository

object AndroidThisDeviceRepositoryTest : Spek({

    val deviceId = "deviceId"
    val deviceName = "deviceName"
    val deviceInfo = emptyMap<String, Serializable>()
    val doorbellData = DoorbellData(deviceId, deviceName, false, deviceInfo)
    val camerasList = listOf(CameraData("cameraId"))
    val ipAddressList = mapOf("InterfaceName" to "IpAddress")
    val context by memoized { mock<Context>() }
    val deviceInfoProvider by memoized {
        mock<DeviceInfoProvider> {
            on { buildDeviceId() }.doReturn(deviceId)
            on { buildDeviceName() }.doReturn(deviceName)
            on { isAndroidThings() }.doReturn(false)
            on { buildDeviceInfo() }.doReturn(deviceInfo)
        }
    }
    val cameraRepository by memoized {
        mock<CameraRepository> {
            on { runBlocking { getCamerasList() } }.doReturn(camerasList)
        }
    }
    val ipAddressProvider by memoized {
        mock<IpAddressProvider> {
            on { runBlocking { getIpAddressList() } }.doReturn(ipAddressList)
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
            it("should be the same deviceId") {
                runBlocking {
                    assertEquals(deviceId, androidThisDeviceRepository.doorbellId())
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
            it("should invoke buildDeviceId") {
                verify(deviceInfoProvider, times(1)).buildDeviceId()
            }
            it("should invoke buildDeviceName") {
                verify(deviceInfoProvider, times(1)).buildDeviceName()
            }
            it("should invoke isAndroidThings") {
                verify(deviceInfoProvider, times(1)).isAndroidThings()
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
