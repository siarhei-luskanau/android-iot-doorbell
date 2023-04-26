package siarhei.luskanau.iot.doorbell.data.model

data class DoorbellData(
    val doorbellId: String,
    val name: String? = null,
    val info: Map<String, String>? = null,
)
