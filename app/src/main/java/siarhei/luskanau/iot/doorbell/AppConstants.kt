package siarhei.luskanau.iot.doorbell

import android.Manifest
import java.text.SimpleDateFormat
import java.util.Locale

object AppConstants {

    const val DEVICE_ID = "device_id"
    const val NAME = "name"
    const val RING = "ring"
    const val IMAGES = "images"

    const val IMAGE_ID = "image_id"
    const val TIMESTAMP = "timestamp"
    const val IMAGE = "image"
    const val ANNOTATIONS = "annotations"
    const val IMAGE_LENGTH = "image_length"

    val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.ENGLISH)

    val PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
}
