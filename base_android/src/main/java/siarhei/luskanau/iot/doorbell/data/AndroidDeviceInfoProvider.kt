package siarhei.luskanau.iot.doorbell.data

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import com.google.android.things.pio.PeripheralManager
import hu.supercluster.paperwork.Paperwork
import java.io.Serializable
import java.util.UUID
import siarhei.luskanau.iot.doorbell.common.DeviceInfoProvider

private const val GIT_SHA = "gitSha"
private const val GIT_BRANCH = "gitBranch"
private const val BUILD_DATE = "buildDate"

class AndroidDeviceInfoProvider(
    private val context: Context
) : DeviceInfoProvider {

    private val paperwork: Paperwork = Paperwork(context)

    @SuppressLint("HardwareIds")
    override fun buildDeviceId(): String {
        val deviceId: String = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        ) ?: UUID.randomUUID().toString()

        return "${deviceId}_${Build.MODEL}"
    }

    override fun buildDeviceName() = Build.MODEL

    override fun isAndroidThings(): Boolean =
        try {
            PeripheralManager.getInstance()
            true
        } catch (t: Throwable) {
            // Timber.e(t)
            false
        }

    override fun buildDeviceInfo() = mapOf<String, Serializable>(
        "DEVICE" to Build.DEVICE,
        "MODEL" to Build.MODEL,
        "SDK_INT" to Build.VERSION.SDK_INT,
        "RELEASE" to Build.VERSION.RELEASE,
        GIT_SHA to paperwork[GIT_SHA],
        GIT_BRANCH to paperwork[GIT_BRANCH],
        BUILD_DATE to paperwork[BUILD_DATE]
    )
}