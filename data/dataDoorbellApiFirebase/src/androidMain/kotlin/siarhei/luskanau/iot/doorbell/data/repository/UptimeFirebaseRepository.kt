package siarhei.luskanau.iot.doorbell.data.repository

import siarhei.luskanau.iot.doorbell.data.model.Uptime

class UptimeFirebaseRepository : BaseFirebaseRepository(), UptimeRepository {

    companion object {
        private const val UPTIME_KEY = "uptime"
        private const val IP_ADDRESS_KEY = "ip_address"
    }

    override suspend fun uptimeStartup(
        doorbellId: String,
        startupTimeMillis: Long,
        startupTimeString: String,
    ) {
        setValueToDatabase(
            getAppDatabase().child(UPTIME_KEY).child(doorbellId)
                .child(UptimeDto.STARTUP_TIME_MILLIS_KEY),
            startupTimeMillis,
        )
        setValueToDatabase(
            getAppDatabase().child(UPTIME_KEY).child(doorbellId)
                .child(UptimeDto.STARTUP_TIME_STRING_KEY),
            startupTimeString,
        )
    }

    override suspend fun uptimePing(
        doorbellId: String,
        pingTimeMillis: Long,
        pingTimeString: String,
    ) {
        setValueToDatabase(
            getAppDatabase().child(UPTIME_KEY).child(doorbellId)
                .child(UptimeDto.PING_TIME_MILLIS_KEY),
            pingTimeMillis,
        )
        setValueToDatabase(
            getAppDatabase().child(UPTIME_KEY).child(doorbellId)
                .child(UptimeDto.PING_TIME_STRING_KEY),
            pingTimeString,
        )
    }

    override suspend fun uptimeRebootRequest(
        doorbellId: String,
        rebootRequestTimeMillis: Long,
        rebootRequestTimeString: String,
    ) {
        setValueToDatabase(
            getAppDatabase().child(UPTIME_KEY).child(doorbellId)
                .child(UptimeDto.REBOOT_REQUEST_TIME_MILLIS_KEY),
            rebootRequestTimeMillis,
        )
        setValueToDatabase(
            getAppDatabase().child(UPTIME_KEY).child(doorbellId)
                .child(UptimeDto.REBOOT_REQUEST_TIME_STRING_KEY),
            rebootRequestTimeString,
        )
    }

    override suspend fun uptimeRebooting(
        doorbellId: String,
        rebootingTimeMillis: Long,
        rebootingTimeString: String,
    ) {
        setValueToDatabase(
            getAppDatabase().child(UPTIME_KEY).child(doorbellId)
                .child(UptimeDto.REBOOTING_TIME_MILLIS_KEY),
            rebootingTimeMillis,
        )
        setValueToDatabase(
            getAppDatabase().child(UPTIME_KEY).child(doorbellId)
                .child(UptimeDto.REBOOTING_TIME_STRING_KEY),
            rebootingTimeString,
        )
    }

    override suspend fun getUptime(doorbellId: String): Uptime? =
        dataSnapshotObject<UptimeDto>(
            getValueFromDatabase(getAppDatabase().child(UPTIME_KEY).child(doorbellId)),
        )?.let { uptimeDto ->
            Uptime(
                startupTimeMillis = uptimeDto.startupTimeMillis,
                startupTimeString = uptimeDto.startupTimeString,
                pingTimeMillis = uptimeDto.pingTimeMillis,
                pingTimeString = uptimeDto.pingTimeString,
                rebootRequestTimeMillis = uptimeDto.rebootRequestTimeMillis,
                rebootRequestTimeString = uptimeDto.rebootRequestTimeString,
                rebootingTimeMillis = uptimeDto.rebootingTimeMillis,
                rebootingTimeString = uptimeDto.rebootingTimeString,
            )
        }

    override suspend fun sendIpAddressMap(
        doorbellId: String,
        ipAddressMap: Map<String, Pair<String, String>>,
    ) =
        setValueToDatabase(
            getAppDatabase().child(UPTIME_KEY).child(doorbellId).child(IP_ADDRESS_KEY),
            ipAddressMap,
        )
}
