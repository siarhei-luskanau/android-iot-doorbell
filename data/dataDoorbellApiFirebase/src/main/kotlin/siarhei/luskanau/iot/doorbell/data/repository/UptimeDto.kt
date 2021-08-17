package siarhei.luskanau.iot.doorbell.data.repository

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UptimeDto(
    @SerialName(STARTUP_TIME_MILLIS_KEY) val startupTimeMillis: Long?,
    @SerialName(STARTUP_TIME_STRING_KEY) val startupTimeString: String?,
    @SerialName(PING_TIME_MILLIS_KEY) val pingTimeMillis: Long?,
    @SerialName(PING_TIME_STRING_KEY) val pingTimeString: String?,
    @SerialName(REBOOT_REQUEST_TIME_MILLIS_KEY) val rebootRequestTimeMillis: Long?,
    @SerialName(REBOOT_REQUEST_TIME_STRING_KEY) val rebootRequestTimeString: String?,
    @SerialName(REBOOTING_TIME_MILLIS_KEY) val rebootingTimeMillis: Long?,
    @SerialName(REBOOTING_TIME_STRING_KEY) val rebootingTimeString: String?
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
