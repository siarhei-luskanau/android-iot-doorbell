package siarhei.luskanau.iot.doorbell.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Uptime(
    val startupTimeMillis: Long?,
    val startupTimeString: String?,
    val pingTimeMillis: Long?,
    val pingTimeString: String?,
    val rebootRequestTimeMillis: Long?,
    val rebootRequestTimeString: String?,
    val rebootingTimeMillis: Long?,
    val rebootingTimeString: String?
) : Parcelable