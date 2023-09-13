package siarhei.luskanau.iot.doorbell.data.repository

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DoorbellDto(
    @SerialName("doorbell_id") val doorbellId: String,
    @SerialName("name") val name: String?,
    @SerialName("info") val info: Map<String, String>?
)
