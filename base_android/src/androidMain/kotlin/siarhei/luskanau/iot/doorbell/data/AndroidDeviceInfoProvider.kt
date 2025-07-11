package siarhei.luskanau.iot.doorbell.data

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import java.util.UUID
import siarhei.luskanau.iot.doorbell.common.DeviceInfoProvider

class AndroidDeviceInfoProvider(private val context: Context) : DeviceInfoProvider {

    @SuppressLint("HardwareIds")
    override fun buildDoorbellId(): String {
        val doorbellId: String = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        ) ?: UUID.randomUUID().toString()

        return "${doorbellId}_${Build.MODEL}"
    }

    override fun buildDeviceName(): String = Build.MODEL

    override fun buildDeviceInfo() = mapOf<String, String>(
        "DEVICE" to Build.DEVICE,
        "MODEL" to Build.MODEL,
        "SDK_INT" to Build.VERSION.SDK_INT.toString(),
        "RELEASE" to Build.VERSION.RELEASE
    )
}
