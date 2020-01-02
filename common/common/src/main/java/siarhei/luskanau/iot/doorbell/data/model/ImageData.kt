package siarhei.luskanau.iot.doorbell.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageData(
    val imageId: String,
    val imageUri: String? = null,
    val timestampString: String = ""
) : Parcelable
