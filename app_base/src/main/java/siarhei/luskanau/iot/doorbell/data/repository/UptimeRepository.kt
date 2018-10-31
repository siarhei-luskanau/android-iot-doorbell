package siarhei.luskanau.iot.doorbell.data.repository

import siarhei.luskanau.iot.doorbell.data.model.Uptime

interface UptimeRepository {

    fun uptimeStartup(
        deviceId: String,
        startupTimeMillis: Long,
        startupTimeString: String
    )

    fun uptimePing(
        deviceId: String,
        pingTimeMillis: Long,
        pingTimeString: String
    )

    fun uptimeRebootRequest(
        deviceId: String,
        rebootRequestTimeMillis: Long,
        rebootRequestTimeString: String
    )

    fun uptimeRebooting(
        deviceId: String,
        rebootingTimeMillis: Long,
        rebootingTimeString: String
    )

    fun getUptime(deviceId: String): Uptime

    fun sendIpAddressMap(deviceId: String, ipAddressMap: Map<String, String>)
}