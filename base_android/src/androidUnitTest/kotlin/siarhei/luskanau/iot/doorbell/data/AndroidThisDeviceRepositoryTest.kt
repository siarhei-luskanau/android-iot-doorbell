package siarhei.luskanau.iot.doorbell.data

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
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
    private val ipAddressList = mapOf("InterfaceName" to Pair("IpAddress", "timestamp"))
    private val context: Context = mockk(relaxed = true, relaxUnitFun = true)
    private val deviceInfoProvider: DeviceInfoProvider =
        mockk(relaxed = true, relaxUnitFun = true) {
            every { buildDoorbellId() } returns doorbellId
            every { buildDeviceName() } returns deviceName
            every { buildDeviceInfo() } returns deviceInfo
        }
    private val cameraRepository: CameraRepository = mockk(relaxed = true, relaxUnitFun = true) {
        every { runBlocking { getCamerasList() } } returns camerasList
    }
    private val ipAddressProvider: IpAddressProvider = mockk(relaxed = true, relaxUnitFun = true) {
        every { runBlocking { getIpAddressList() } } returns ipAddressList
    }

    private val androidThisDeviceRepository = AndroidThisDeviceRepository(
        context = context,
        deviceInfoProvider = deviceInfoProvider,
        cameraRepository = cameraRepository,
        ipAddressProvider = ipAddressProvider
    )

    @Test
    fun `test check doorbellId`() {
        runTest {
            assertEquals(
                expected = doorbellId,
                actual = androidThisDeviceRepository.doorbellId(),
                message = "should be the same doorbellI"
            )
        }
    }

    @Test
    fun `test check doorbellData`() {
        runTest {
            val resultDoorbellData: DoorbellData = androidThisDeviceRepository.doorbellData()
            verify { deviceInfoProvider.buildDoorbellId() }
            verify { deviceInfoProvider.buildDeviceName() }
            verify { deviceInfoProvider.buildDeviceInfo() }
            assertEquals(
                expected = doorbellData,
                actual = resultDoorbellData,
                message = "should be the same DoorbellData"
            )
        }
    }

    @Test
    fun `test check getCamerasList`() {
        runTest {
            val resultCamerasList = androidThisDeviceRepository.getCamerasList()
            verify { runBlocking { cameraRepository.getCamerasList() } }
            assertEquals(
                expected = camerasList,
                actual = resultCamerasList,
                message = "should be the same list"
            )
        }
    }

    @Test
    fun `test check getIpAddressList`() {
        runTest {
            val resultIpAddressList = androidThisDeviceRepository.getIpAddressList()
            verify { runBlocking { ipAddressProvider.getIpAddressList() } }
            assertEquals(
                expected = ipAddressList,
                actual = resultIpAddressList,
                message = "should be the same list"
            )
        }
    }
}
