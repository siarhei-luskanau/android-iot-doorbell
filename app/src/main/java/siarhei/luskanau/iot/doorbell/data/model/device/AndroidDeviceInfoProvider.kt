package siarhei.luskanau.iot.doorbell.data.model.device

import android.content.Context
import android.os.Build
import android.provider.Settings
import javax.inject.Inject

class AndroidDeviceInfoProvider @Inject constructor(
        private val context: Context
) : DeviceInfoProvider {

    override fun buildDeviceId() =
            Settings.Secure.getString(
                    context.getContentResolver(), Settings.Secure.ANDROID_ID
            ) + "_" + Build.MODEL

    override fun buildDeviceName() = Build.MODEL

    override fun buildDeviceInfo() = mapOf<String, Any>(
            Pair("DEVICE", Build.DEVICE),
            Pair("MODEL", Build.MODEL),
            Pair("SDK_INT", Build.VERSION.SDK_INT),
            Pair("RELEASE", Build.VERSION.RELEASE)
    )

}