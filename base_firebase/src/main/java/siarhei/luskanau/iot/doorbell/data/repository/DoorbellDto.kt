package siarhei.luskanau.iot.doorbell.data.repository

import com.google.gson.annotations.SerializedName

data class DoorbellDto(
        @SerializedName("doorbell_id") val doorbellId: String,
        @SerializedName("name") val name: String = "",
        @SerializedName("is_android_things") val isAndroidThings: Boolean = false,
        @SerializedName("info") val info: Map<String, Any> = emptyMap()
)
