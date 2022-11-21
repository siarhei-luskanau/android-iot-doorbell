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

    override fun isEmulator(): Boolean =
        Build.FINGERPRINT.contains("generic")

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
