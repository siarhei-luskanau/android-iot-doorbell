package siarhei.luskanau.iot.doorbell.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class DoorbellData(
    val doorbellId: String,
    val name: String? = null,
    val isAndroidThings: Boolean = false,
    val info: Map<String, Serializable>? = null
) : Parcelable
