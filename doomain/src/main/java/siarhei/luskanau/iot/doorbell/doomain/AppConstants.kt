package siarhei.luskanau.iot.doorbell.doomain

import android.Manifest
import java.text.SimpleDateFormat
import java.util.Locale

object AppConstants {

    val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.ENGLISH)

    val PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
}
