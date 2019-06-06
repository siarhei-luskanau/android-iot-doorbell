package siarhei.luskanau.iot.doorbell.data.repository

import siarhei.luskanau.iot.doorbell.data.model.Uptime

interface UptimeRepository {

    suspend fun uptimeStartup(
        deviceId: String,
        startupTimeMillis: Long,
        startupTimeString: String
    )

    suspend fun uptimePing(
        deviceId: String,
        pingTimeMillis: Long,
        pingTimeString: String
    )

    suspend fun uptimeRebootRequest(
        doorbellId: String,
        rebootRequestTimeMillis: Long,
        rebootRequestTimeString: String
    )

    suspend fun uptimeRebooting(
        deviceId: String,
        rebootingTimeMillis: Long,
        rebootingTimeString: String
    )

    suspend fun getUptime(deviceId: String): Uptime

    suspend fun sendIpAddressMap(deviceId: String, ipAddressMap: Map<String, String>)
}