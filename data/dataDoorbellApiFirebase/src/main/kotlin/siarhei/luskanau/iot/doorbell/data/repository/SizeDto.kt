package siarhei.luskanau.iot.doorbell.data.repository

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SizeDto(
    @Json(name = "width") val width: Int,
    @Json(name = "height") val height: Int
)
