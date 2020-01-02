package siarhei.luskanau.iot.doorbell.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageFile(
    val name: String? = null,
    val path: String? = null,
    val size: Long? = null,
    val throwable: Throwable? = null
) : Parcelable
