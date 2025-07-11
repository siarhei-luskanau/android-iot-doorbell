package siarhei.luskanau.iot.doorbell.data.repository

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SizeDto(@SerialName("width") val width: Int, @SerialName("height") val height: Int)
