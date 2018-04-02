package siarhei.luskanau.iot.doorbell

import android.Manifest
import java.text.SimpleDateFormat
import java.util.*

object AppConstants {

    val DEVICE_ID = "device_id"
    val NAME = "name"
    val RING = "ring"
    val IMAGES = "images"

    val IMAGE_ID = "image_id"
    val TIMESTAMP = "timestamp"
    val IMAGE = "image"
    val ANNOTATIONS = "annotations"
    val IMAGE_LENGTH = "image_length"

    val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.ENGLISH)

    val PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

}
