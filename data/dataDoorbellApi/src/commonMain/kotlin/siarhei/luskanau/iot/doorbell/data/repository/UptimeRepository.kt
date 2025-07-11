package siarhei.luskanau.iot.doorbell.data.repository

import siarhei.luskanau.iot.doorbell.data.model.Uptime

interface UptimeRepository {

    suspend fun uptimeStartup(
        doorbellId: String,
        startupTimeMillis: Long,
        startupTimeString: String
    )

    suspend fun uptimePing(doorbellId: String, pingTimeMillis: Long, pingTimeString: String)

    suspend fun uptimeRebootRequest(
        doorbellId: String,
        rebootRequestTimeMillis: Long,
        rebootRequestTimeString: String
    )

    suspend fun uptimeRebooting(
        doorbellId: String,
        rebootingTimeMillis: Long,
        rebootingTimeString: String
    )

    suspend fun getUptime(doorbellId: String): Uptime?

    suspend fun sendIpAddressMap(
        doorbellId: String,
        ipAddressMap: Map<String, Pair<String, String>>
    )
}
