package siarhei.luskanau.iot.doorbell.data.repository

import siarhei.luskanau.iot.doorbell.data.model.Uptime

class StubUptimeRepository : UptimeRepository {

    override suspend fun uptimeStartup(
        doorbellId: String,
        startupTimeMillis: Long,
        startupTimeString: String
    ) = Unit

    override suspend fun uptimePing(
        doorbellId: String,
        pingTimeMillis: Long,
        pingTimeString: String
    ) = Unit

    override suspend fun uptimeRebootRequest(
        doorbellId: String,
        rebootRequestTimeMillis: Long,
        rebootRequestTimeString: String
    ) = Unit

    override suspend fun uptimeRebooting(
        doorbellId: String,
        rebootingTimeMillis: Long,
        rebootingTimeString: String
    ) = Unit

    override suspend fun getUptime(doorbellId: String): Uptime? = null

    override suspend fun sendIpAddressMap(
        doorbellId: String,
        ipAddressMap: Map<String, Pair<String, String>>
    ) = Unit
}
