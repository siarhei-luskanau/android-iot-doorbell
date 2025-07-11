package siarhei.luskanau.iot.doorbell.data.repository

import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import siarhei.luskanau.iot.doorbell.data.model.Uptime

class UptimeFirebaseRepository :
    BaseFirebaseRepository(),
    UptimeRepository {

    companion object {
        private const val UPTIME_KEY = "uptime"
        private const val IP_ADDRESS_KEY = "ip_address"
    }

    override suspend fun uptimeStartup(
        doorbellId: String,
        startupTimeMillis: Long,
        startupTimeString: String
    ) {
        getAppDatabase().child(UPTIME_KEY).child(doorbellId)
            .child(UptimeDto.STARTUP_TIME_MILLIS_KEY).setValue(startupTimeMillis)
        getAppDatabase().child(UPTIME_KEY).child(doorbellId)
            .child(UptimeDto.STARTUP_TIME_STRING_KEY).setValue(startupTimeString)
    }

    override suspend fun uptimePing(
        doorbellId: String,
        pingTimeMillis: Long,
        pingTimeString: String
    ) {
        getAppDatabase().child(UPTIME_KEY).child(doorbellId)
            .child(UptimeDto.PING_TIME_MILLIS_KEY).setValue(pingTimeMillis)
        getAppDatabase().child(UPTIME_KEY).child(doorbellId)
            .child(UptimeDto.PING_TIME_STRING_KEY).setValue(pingTimeString)
    }

    override suspend fun uptimeRebootRequest(
        doorbellId: String,
        rebootRequestTimeMillis: Long,
        rebootRequestTimeString: String
    ) {
        getAppDatabase().child(UPTIME_KEY).child(doorbellId)
            .child(UptimeDto.REBOOT_REQUEST_TIME_MILLIS_KEY).setValue(rebootRequestTimeMillis)
        getAppDatabase().child(UPTIME_KEY).child(doorbellId)
            .child(UptimeDto.REBOOT_REQUEST_TIME_STRING_KEY).setValue(rebootRequestTimeString)
    }

    override suspend fun uptimeRebooting(
        doorbellId: String,
        rebootingTimeMillis: Long,
        rebootingTimeString: String
    ) {
        getAppDatabase().child(UPTIME_KEY).child(doorbellId)
            .child(UptimeDto.REBOOTING_TIME_MILLIS_KEY).setValue(rebootingTimeMillis)
        getAppDatabase().child(UPTIME_KEY).child(doorbellId)
            .child(UptimeDto.REBOOTING_TIME_STRING_KEY).setValue(rebootingTimeString)
    }

    override suspend fun getUptime(doorbellId: String): Uptime? =
        getAppDatabase().child(UPTIME_KEY).child(doorbellId).valueEvents
            .map { it.value<UptimeDto>() }
            .map { uptimeDto ->
                Uptime(
                    startupTimeMillis = uptimeDto.startupTimeMillis,
                    startupTimeString = uptimeDto.startupTimeString,
                    pingTimeMillis = uptimeDto.pingTimeMillis,
                    pingTimeString = uptimeDto.pingTimeString,
                    rebootRequestTimeMillis = uptimeDto.rebootRequestTimeMillis,
                    rebootRequestTimeString = uptimeDto.rebootRequestTimeString,
                    rebootingTimeMillis = uptimeDto.rebootingTimeMillis,
                    rebootingTimeString = uptimeDto.rebootingTimeString
                )
            }.firstOrNull()

    override suspend fun sendIpAddressMap(
        doorbellId: String,
        ipAddressMap: Map<String, Pair<String, String>>
    ) = getAppDatabase().child(UPTIME_KEY).child(doorbellId).child(IP_ADDRESS_KEY)
        .setValue(ipAddressMap)
}
