package siarhei.luskanau.iot.doorbell.data.model

import com.google.gson.annotations.SerializedName

data class DoorbellData(
        @SerializedName("doorbell_id") val doorbellId: String,
        @SerializedName("name") val name: String = "",
        @SerializedName("is_android_things") val isAndroidThings: Boolean = false,
        @SerializedName("info") val info: Map<String, Any> = emptyMap()
)
