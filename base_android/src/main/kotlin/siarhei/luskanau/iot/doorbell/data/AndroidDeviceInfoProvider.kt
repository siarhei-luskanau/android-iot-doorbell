package siarhei.luskanau.iot.doorbell.data

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import hu.supercluster.paperwork.Paperwork
import siarhei.luskanau.iot.doorbell.common.DeviceInfoProvider
import java.util.UUID

private const val GIT_SHA = "gitSha"
private const val GIT_BRANCH = "gitBranch"
private const val BUILD_DATE = "buildDate"

class AndroidDeviceInfoProvider(
    private val context: Context,
) : DeviceInfoProvider {

    private val paperwork: Paperwork = Paperwork(context)

    @SuppressLint("HardwareIds")
    override fun buildDoorbellId(): String {
        val doorbellId: String = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID,
        ) ?: UUID.randomUUID().toString()

        return "${doorbellId}_${Build.MODEL}"
    }

    override fun buildDeviceName(): String = Build.MODEL

    override fun buildDeviceInfo() = mapOf<String, String>(
        "DEVICE" to Build.DEVICE,
        "MODEL" to Build.MODEL,
        "SDK_INT" to Build.VERSION.SDK_INT.toString(),
        "RELEASE" to Build.VERSION.RELEASE,
        GIT_SHA to paperwork[GIT_SHA],
        GIT_BRANCH to paperwork[GIT_BRANCH],
        BUILD_DATE to paperwork[BUILD_DATE],
    )
}
