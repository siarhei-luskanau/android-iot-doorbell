package siarhei.luskanau.iot.doorbell.data.repository

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DoorbellDto(
    @Json(name = "doorbell_id") val doorbellId: String,
    @Json(name = "name") val name: String?,
    @Json(name = "info") val info: Map<String, Any>?
)
