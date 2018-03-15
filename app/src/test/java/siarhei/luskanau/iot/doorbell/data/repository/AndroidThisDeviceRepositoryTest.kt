package siarhei.luskanau.iot.doorbell.data.repository

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import junit.framework.Assert
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.camera.CameraDataProvider
import siarhei.luskanau.iot.doorbell.data.model.device.DeviceInfoProvider
import siarhei.luskanau.iot.doorbell.data.model.ipaddress.IpAddressProvider

object AndroidThisDeviceRepositoryTest : Spek({

    val deviceId = "deviceId"
    val deviceName = "deviceName"
    val deviceInfo = emptyMap<String, Any>()
    val doorbellData = DoorbellData(deviceId, deviceName, deviceInfo)
    val camerasList = listOf(CameraData("cameraId"))
    val ipAddressList = listOf(Pair("InterfaceName", "IpAddress"))
    val deviceInfoProvider by memoized {
        mock<DeviceInfoProvider> {
            on { buildDeviceId() }.doReturn(deviceId)
            on { buildDeviceName() }.doReturn(deviceName)
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
                deviceInfoProvider,
                cameraDataProvider,
                ipAddressProvider
        )
    }

    describe("a AndroidThisDeviceRepository") {

        context("check doorbellId") {
            it("should be the same deviceId") {
                Assert.assertEquals(deviceId, androidThisDeviceRepository.doorbellId())
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
            it("should invoke buildDeviceInfo") {
                verify(deviceInfoProvider, times(1)).buildDeviceInfo()
            }
            it("should be the same DoorbellData") {
                Assert.assertEquals(doorbellData, resultDoorbellData)
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
                Assert.assertEquals(camerasList, resultCamerasList)
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
                Assert.assertEquals(ipAddressList, resultIpAddressList)
            }
        }

    }

})