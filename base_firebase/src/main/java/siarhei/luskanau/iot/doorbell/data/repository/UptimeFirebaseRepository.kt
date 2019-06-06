package siarhei.luskanau.iot.doorbell.data.repository

import siarhei.luskanau.iot.doorbell.data.model.Uptime

class UptimeFirebaseRepository : BaseFirebaseRepository(), UptimeRepository {

    companion object {
        private const val UPTIME_KEY = "uptime"
        private const val IP_ADDRESS_KEY = "ip_address"
    }

    override suspend fun uptimeStartup(
        deviceId: String,
        startupTimeMillis: Long,
        startupTimeString: String
    ) {
        setValueToDatabase(
            getAppDatabase().child(UPTIME_KEY).child(deviceId)
                .child(UptimeDto.STARTUP_TIME_MILLIS_KEY),
            startupTimeMillis
        )
        setValueToDatabase(
            getAppDatabase().child(UPTIME_KEY).child(deviceId)
                .child(UptimeDto.STARTUP_TIME_STRING_KEY),
            startupTimeString
        )
    }

    override suspend fun uptimePing(
        deviceId: String,
        pingTimeMillis: Long,
        pingTimeString: String
    ) {
        setValueToDatabase(
            getAppDatabase().child(UPTIME_KEY).child(deviceId)
                .child(UptimeDto.PING_TIME_MILLIS_KEY),
            pingTimeMillis
        )
        setValueToDatabase(
            getAppDatabase().child(UPTIME_KEY).child(deviceId)
                .child(UptimeDto.PING_TIME_STRING_KEY),
            pingTimeString
        )
    }

    override suspend fun uptimeRebootRequest(
        doorbellId: String,
        rebootRequestTimeMillis: Long,
        rebootRequestTimeString: String
    ) {
        setValueToDatabase(
            getAppDatabase().child(UPTIME_KEY).child(doorbellId)
                .child(UptimeDto.REBOOT_REQUEST_TIME_MILLIS_KEY),
            rebootRequestTimeMillis
        )
        setValueToDatabase(
            getAppDatabase().child(UPTIME_KEY).child(doorbellId)
                .child(UptimeDto.REBOOT_REQUEST_TIME_STRING_KEY),
            rebootRequestTimeString
        )
    }

    override suspend fun uptimeRebooting(
        deviceId: String,
        rebootingTimeMillis: Long,
        rebootingTimeString: String
    ) {
        setValueToDatabase(
            getAppDatabase().child(UPTIME_KEY).child(deviceId)
                .child(UptimeDto.REBOOTING_TIME_MILLIS_KEY),
            rebootingTimeMillis
        )
        setValueToDatabase(
            getAppDatabase().child(UPTIME_KEY).child(deviceId)
                .child(UptimeDto.REBOOTING_TIME_STRING_KEY),
            rebootingTimeString
        )
    }

    override suspend fun getUptime(deviceId: String): Uptime {
        val uptimeDto = dataSnapshotObject(
            getValueFromDatabase(getAppDatabase().child(UPTIME_KEY).child(deviceId)),
            UptimeDto::class.java
        )

        return Uptime(
            startupTimeMillis = uptimeDto.startupTimeMillis,
            startupTimeString = uptimeDto.startupTimeString,
            pingTimeMillis = uptimeDto.pingTimeMillis,
            pingTimeString = uptimeDto.pingTimeString,
            rebootRequestTimeMillis = uptimeDto.rebootRequestTimeMillis,
            rebootRequestTimeString = uptimeDto.rebootRequestTimeString,
            rebootingTimeMillis = uptimeDto.rebootingTimeMillis,
            rebootingTimeString = uptimeDto.rebootingTimeString

        )
    }

    override suspend fun sendIpAddressMap(deviceId: String, ipAddressMap: Map<String, String>) =
        setValueToDatabase(
            getAppDatabase().child(UPTIME_KEY).child(deviceId).child(IP_ADDRESS_KEY),
            serializeByGson(ipAddressMap)
        )
}