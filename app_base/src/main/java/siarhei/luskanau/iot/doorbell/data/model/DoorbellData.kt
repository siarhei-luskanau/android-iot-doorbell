package siarhei.luskanau.iot.doorbell.data.model

import com.google.gson.annotations.SerializedName

data class DoorbellData(@SerializedName("doorbell_id") val doorbellId: String)
