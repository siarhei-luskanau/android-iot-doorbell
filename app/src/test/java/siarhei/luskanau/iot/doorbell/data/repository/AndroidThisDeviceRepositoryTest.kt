package siarhei.luskanau.iot.doorbell.data.repository

import android.content.Context
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.Assert.assertEquals
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.camera.CameraDataProvider
import siarhei.luskanau.iot.doorbell.data.model.device.DeviceInfoProvider
import siarhei.luskanau.iot.doorbell.data.model.ipaddress.IpAddressProvider

object AndroidThisDeviceRepositoryTest : Spek({

    val deviceId = "deviceId"
    val deviceName = "deviceName"
    val deviceInfo = emptyMap<String, Any>()
    val doorbellData = DoorbellData(deviceId, deviceName, false, deviceInfo)
    val camerasList = listOf(CameraData("cameraId"))
    val ipAddressList = listOf(Pair("InterfaceName", "IpAddress"))
    val context by memoized { mock<Context> {} }
    val deviceInfoProvider by memoized {
        mock<DeviceInfoProvider> {
            on { buildDeviceId() }.doReturn(deviceId)
            on { buildDeviceName() }.doReturn(deviceName)
            on { isAndroidThings() }.doReturn(false)
            on { buildDeviceInfo() }.doReturn(deviceInfo)
        }
    }
    val cameraDataProvider by memoized {
        mock<CameraDataProvider> {
            on { getCamerasList() }.doReturn(camerasList)
        }
    }
    val ipAddressProvider by memoized {
        mock<IpAddressProvider> {
            on { getIpAddressList() }.doReturn(ipAddressList)
        }
    }

    val androidThisDeviceRepository by memoized {
        AndroidThisDeviceRepository(
                context,
                deviceInfoProvider,
                cameraDataProvider,
                ipAddressProvider
        )
    }

    describe("a AndroidThisDeviceRepository") {

        context("check doorbellId") {
            it("should be the same deviceId") {
                assertEquals(deviceId, androidThisDeviceRepository.doorbellId())
            }
        }

        context("check doorbellData") {
            var resultDoorbellData: DoorbellData? = null
            beforeEachTest {
                resultDoorbellData = androidThisDeviceRepository.doorbellData()
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
                resultCamerasList = androidThisDeviceRepository.getCamerasList()
            }
            it("should invoke ") {
                verify(cameraDataProvider, times(1)).getCamerasList()
            }
            it("should be the same list") {
                assertEquals(camerasList, resultCamerasList)
            }
        }

        context("check getIpAddressList") {
            var resultIpAddressList: List<Pair<String, String>>? = null
            beforeEachTest {
                resultIpAddressList = androidThisDeviceRepository.getIpAddressList()
            }
            it("should invoke ") {
                verify(ipAddressProvider, times(1)).getIpAddressList()
            }
            it("should be the same list") {
                assertEquals(ipAddressList, resultIpAddressList)
            }
        }

    }

})