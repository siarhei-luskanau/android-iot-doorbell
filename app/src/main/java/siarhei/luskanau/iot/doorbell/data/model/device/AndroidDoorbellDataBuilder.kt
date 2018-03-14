package siarhei.luskanau.iot.doorbell.data.model.device

import android.content.Context
import android.os.Build
import android.provider.Settings
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import javax.inject.Inject

class AndroidDoorbellDataBuilder @Inject constructor(private val context: Context) : DoorbellDataBuilder {

    override fun buildDoorbellData(): DoorbellData {
        val doorbellId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID)

        val name = Build.MODEL

        val info = mutableMapOf<String, Any>()
        info["DEVICE"] = Build.DEVICE
        info["MODEL"] = Build.MODEL
        info["SDK_INT"] = Build.VERSION.SDK_INT
        info["RELEASE"] = Build.VERSION.RELEASE

        return DoorbellData(doorbellId, name, info)
    }

}