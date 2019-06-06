package siarhei.luskanau.iot.doorbell.data

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.repository.CameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import siarhei.luskanau.iot.doorbell.doomain.AppConstants
import siarhei.luskanau.iot.doorbell.doomain.DeviceInfoProvider
import siarhei.luskanau.iot.doorbell.doomain.IpAddressProvider
import timber.log.Timber

class AndroidThisDeviceRepository(
    private val context: Context,
    private val deviceInfoProvider: DeviceInfoProvider,
    private val cameraRepository: CameraRepository,
    private val ipAddressProvider: IpAddressProvider
) : ThisDeviceRepository {

    private val doorbellData = DoorbellData(
        deviceInfoProvider.buildDeviceId(),
        deviceInfoProvider.buildDeviceName(),
        deviceInfoProvider.isAndroidThings(),
        deviceInfoProvider.buildDeviceInfo()
    )

    override suspend fun doorbellId() =
        deviceInfoProvider.buildDeviceId()

    override suspend fun doorbellData() =
        doorbellData

    override suspend fun getCamerasList() =
        cameraRepository.getCamerasList()

    override suspend fun getIpAddressList() =
        ipAddressProvider.getIpAddressList()

    override fun reboot() {
        try {
            Timber.d("reboot")
            Runtime.getRuntime().exec("reboot")
        } catch (t: Throwable) {
            Timber.e(t)
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
