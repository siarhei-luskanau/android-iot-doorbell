package siarhei.luskanau.iot.doorbell.data

import android.content.Context
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import siarhei.luskanau.iot.doorbell.common.DeviceInfoProvider
import siarhei.luskanau.iot.doorbell.common.IpAddressProvider
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.repository.CameraRepository

class AndroidThisDeviceRepositoryTest {

    private val doorbellId = "doorbellId"
    private val deviceName = "deviceName"
    private val deviceInfo = emptyMap<String, String>()
    private val doorbellData = DoorbellData(doorbellId, deviceName, deviceInfo)
    private val camerasList = listOf(CameraData("cameraId"))
    private val ipAddressList = mapOf("InterfaceName" to "IpAddress")
    private val context = mock(Context::class.java)
    private val deviceInfoProvider = mock(DeviceInfoProvider::class.java).apply {
        given(buildDoorbellId()).willReturn(doorbellId)
        given(buildDeviceName()).willReturn(deviceName)
        given(buildDeviceInfo()).willReturn(deviceInfo)
    }
    private val cameraRepository = mock(CameraRepository::class.java).apply {
        given(runBlocking { getCamerasList() }).willReturn(camerasList)
    }
    private val ipAddressProvider = mock(IpAddressProvider::class.java).apply {
        given(runBlocking { getIpAddressList() }).willReturn(ipAddressList)
    }

    private val androidThisDeviceRepository = AndroidThisDeviceRepository(
        context = context,
        deviceInfoProvider = deviceInfoProvider,
        cameraRepository = cameraRepository,
        ipAddressProvider = ipAddressProvider
    )

    @Test
    fun `test check doorbellId`() {
        runBlocking {
            assertEquals(
                expected = doorbellId,
                actual = androidThisDeviceRepository.doorbellId(),
                message = "should be the same doorbellI"
            )
        }
    }

    @Test
    fun `test check doorbellData`() {
        runBlockingTest {
            val resultDoorbellData: DoorbellData = androidThisDeviceRepository.doorbellData()
            verify(deviceInfoProvider, times(1)).buildDoorbellId()
            verify(deviceInfoProvider, times(1)).buildDeviceName()
            verify(deviceInfoProvider, times(1)).buildDeviceInfo()
            assertEquals(
                expected = doorbellData,
                actual = resultDoorbellData,
                message = "should be the same DoorbellData"
            )
        }
    }

    @Test
    fun `test check getCamerasList`() {
        runBlockingTest {
            val resultCamerasList = androidThisDeviceRepository.getCamerasList()
            verify(cameraRepository, times(1)).getCamerasList()
            assertEquals(
                expected = camerasList,
                actual = resultCamerasList,
                message = "should be the same list",
            )
        }
    }

    @Test
    fun `test check getIpAddressList`() {
        runBlockingTest {
            val resultIpAddressList = androidThisDeviceRepository.getIpAddressList()
            verify(ipAddressProvider, times(1)).getIpAddressList()
            assertEquals(
                expected = ipAddressList,
                actual = resultIpAddressList,
                message = "should be the same list"
            )
        }
    }
}
