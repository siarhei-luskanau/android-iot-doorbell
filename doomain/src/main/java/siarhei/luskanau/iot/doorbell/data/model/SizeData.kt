package siarhei.luskanau.iot.doorbell.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SizeData(
    val width: Int,
    val height: Int
) : Parcelable
