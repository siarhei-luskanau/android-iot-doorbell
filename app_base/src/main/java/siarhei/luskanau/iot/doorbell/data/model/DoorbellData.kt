package siarhei.luskanau.iot.doorbell.data.model

data class DoorbellData(
    val doorbellId: String,
    val name: String = "",
    val isAndroidThings: Boolean = false,
    val info: Map<String, Any> = emptyMap()
)
