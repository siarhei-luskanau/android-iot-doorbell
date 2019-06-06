package siarhei.luskanau.iot.doorbell.data.model

import android.os.Parcelable
import android.util.Size
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class CameraData(
    val cameraId: String,
    val name: String? = null,
    val sizes: Map<Int, Size>? = null,
    val info: Map<String, Serializable>? = null,
    val cameraxInfo: Map<String, Serializable>? = null
) : Parcelable
