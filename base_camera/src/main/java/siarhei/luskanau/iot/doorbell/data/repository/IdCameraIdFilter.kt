package siarhei.luskanau.iot.doorbell.data.repository

import android.annotation.SuppressLint
import androidx.camera.core.CameraIdFilter

@SuppressLint("RestrictedApi")
class IdCameraIdFilter(private val cameraId: String) : CameraIdFilter {

    override fun filter(cameraIds: MutableSet<String>): MutableSet<String> =
            if (cameraIds.contains(cameraId)) {
                mutableSetOf(cameraId)
            } else {
                mutableSetOf()
            }
}
