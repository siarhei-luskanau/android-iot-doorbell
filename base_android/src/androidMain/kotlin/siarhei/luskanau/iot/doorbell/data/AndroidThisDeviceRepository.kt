package siarhei.luskanau.iot.doorbell.data

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import siarhei.luskanau.iot.doorbell.common.AppConstants
import siarhei.luskanau.iot.doorbell.common.DeviceInfoProvider
import siarhei.luskanau.iot.doorbell.common.IpAddressProvider
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.repository.CameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import timber.log.Timber

class AndroidThisDeviceRepository(
    private val context: Context,
    private val deviceInfoProvider: DeviceInfoProvider,
    private val cameraRepository: CameraRepository,
    private val ipAddressProvider: IpAddressProvider
) : ThisDeviceRepository {

    @Suppress("CyclomaticComplexMethod")
    override fun isEmulator(): Boolean =
        (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")) ||
            Build.FINGERPRINT.startsWith("generic") ||
            Build.FINGERPRINT.startsWith("unknown") ||
            Build.HARDWARE.contains("goldfish") ||
            Build.HARDWARE.contains("ranchu") ||
            Build.MODEL.contains("google_sdk") ||
            Build.MODEL.contains("Emulator") ||
            Build.MODEL.contains("Android SDK built for x86") ||
            Build.MANUFACTURER.contains("Genymotion") ||
            Build.PRODUCT.contains("sdk_google") ||
            Build.PRODUCT.contains("google_sdk") ||
            Build.PRODUCT.contains("sdk") ||
            Build.PRODUCT.contains("sdk_x86") ||
            Build.PRODUCT.contains("sdk_gphone64_arm64") ||
            Build.PRODUCT.contains("vbox86p") ||
            Build.PRODUCT.contains("emulator") ||
            Build.PRODUCT.contains("simulator")

    private val doorbellData = DoorbellData(
        deviceInfoProvider.buildDoorbellId(),
        deviceInfoProvider.buildDeviceName(),
        deviceInfoProvider.buildDeviceInfo()
    )

    override suspend fun doorbellId() =
        deviceInfoProvider.buildDoorbellId()

    override suspend fun doorbellData() =
        doorbellData

    override suspend fun getCamerasList() =
        cameraRepository.getCamerasList()

    override suspend fun getIpAddressList() =
        ipAddressProvider.getIpAddressList()

    override fun reboot() {
        runCatching {
            Timber.d("reboot")
            Runtime.getRuntime().exec("reboot")
        }.onFailure {
            Timber.e(it)
        }
    }

    override fun isPermissionsGranted(): Boolean {
        for (permission in AppConstants.PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }
}
