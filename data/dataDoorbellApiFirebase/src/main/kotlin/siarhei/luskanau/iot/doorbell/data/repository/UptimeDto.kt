package siarhei.luskanau.iot.doorbell.data.repository

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UptimeDto(
    @Json(name = STARTUP_TIME_MILLIS_KEY) val startupTimeMillis: Long?,
    @Json(name = STARTUP_TIME_STRING_KEY) val startupTimeString: String?,
    @Json(name = PING_TIME_MILLIS_KEY) val pingTimeMillis: Long?,
    @Json(name = PING_TIME_STRING_KEY) val pingTimeString: String?,
    @Json(name = REBOOT_REQUEST_TIME_MILLIS_KEY) val rebootRequestTimeMillis: Long?,
    @Json(name = REBOOT_REQUEST_TIME_STRING_KEY) val rebootRequestTimeString: String?,
    @Json(name = REBOOTING_TIME_MILLIS_KEY) val rebootingTimeMillis: Long?,
    @Json(name = REBOOTING_TIME_STRING_KEY) val rebootingTimeString: String?
) {

    companion object {
        const val STARTUP_TIME_MILLIS_KEY = "startup_time_millis"
        const val STARTUP_TIME_STRING_KEY = "startup_time_string"
        const val PING_TIME_MILLIS_KEY = "ping_time_millis"
        const val PING_TIME_STRING_KEY = "ping_time_string"
        const val REBOOT_REQUEST_TIME_MILLIS_KEY = "reboot_request_time_millis"
        const val REBOOT_REQUEST_TIME_STRING_KEY = "reboot_request_time_string"
        const val REBOOTING_TIME_MILLIS_KEY = "rebooting_time_millis"
        const val REBOOTING_TIME_STRING_KEY = "rebooting_time_string"
    }
}
