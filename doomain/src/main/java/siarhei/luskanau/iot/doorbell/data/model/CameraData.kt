package siarhei.luskanau.iot.doorbell.data.model

import android.os.Parcelable
import java.io.Serializable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CameraData(
    val cameraId: String,
    val name: String? = null,
    val sizes: Map<Int, SizeData>? = null,
    val info: Map<String, Serializable>? = null,
    val cameraxInfo: Map<String, Serializable>? = null
) : Parcelable
