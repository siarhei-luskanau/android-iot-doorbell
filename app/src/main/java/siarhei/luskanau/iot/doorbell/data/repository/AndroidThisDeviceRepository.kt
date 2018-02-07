package siarhei.luskanau.iot.doorbell.data.repository

import android.content.Context
import android.hardware.camera2.CameraManager
import android.provider.Settings
import io.reactivex.Single
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import timber.log.Timber
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*

class AndroidThisDeviceRepository(private val context: Context) : ThisDeviceRepository {

    private val soorbellData: DoorbellData

    init {
        soorbellData = DoorbellData(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID))
    }

    override fun doorbellId() = soorbellData.doorbellId

    override fun doorbellData(): DoorbellData = soorbellData

    override fun getCamerasList(): Single<List<CameraData>> {
        val list = mutableListOf<CameraData>()

        try {
            (context.getSystemService(Context.CAMERA_SERVICE) as CameraManager?)?.let { cameraManager ->
                cameraManager.cameraIdList
                        .map { cameraId: String ->
                            list.add(CameraData(cameraId))
                        }
            }
        } catch (t: Throwable) {
            Timber.e(t)
        }

        return Single.just(list)
    }

    override fun getIpAddressMap(): Single<Map<String, String>> {
        val ipAddressMap = mutableMapOf<String, String>()

        try {
            for (networkInterface in Collections.list(NetworkInterface.getNetworkInterfaces())) {
                Collections.list(networkInterface.inetAddresses)
                        .filter { inetAddress: InetAddress ->
                            !inetAddress.isLoopbackAddress && inetAddress is Inet4Address
                        }
                        .map { inetAddress: InetAddress ->
                            inetAddress.hostAddress + " " + Date()
                        }
                        .forEach { hostAddress: String ->
                            ipAddressMap[networkInterface.name] = hostAddress
                        }
            }
        } catch (e: Exception) {
            Timber.e(e)
        }

        return Single.just(ipAddressMap)
    }

}
