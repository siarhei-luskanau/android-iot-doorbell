package siarhei.luskanau.iot.doorbell.data.repository

import kotlinx.coroutines.runBlocking
import siarhei.luskanau.iot.doorbell.data.model.Uptime

class UptimeFirebaseRepository : BaseFirebaseRepository(), UptimeRepository {

    companion object {
        private const val UPTIME_KEY = "uptime"
        private const val IP_ADDRESS_KEY = "ip_address"
    }

    override fun uptimeStartup(
        deviceId: String,
        startupTimeMillis: Long,
        startupTimeString: String
    ) {
        runBlocking {
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
    }

    override fun uptimePing(
        deviceId: String,
        pingTimeMillis: Long,
        pingTimeString: String
    ) {
        runBlocking {
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
    }

    override suspend fun uptimeRebootRequest(
        deviceId: String,
        rebootRequestTimeMillis: Long,
        rebootRequestTimeString: String
    ) {
        setValueToDatabase(
                getAppDatabase().child(UPTIME_KEY).child(deviceId)
                        .child(UptimeDto.REBOOT_REQUEST_TIME_MILLIS_KEY),
                rebootRequestTimeMillis
        )
        setValueToDatabase(
                getAppDatabase().child(UPTIME_KEY).child(deviceId)
                        .child(UptimeDto.REBOOT_REQUEST_TIME_STRING_KEY),
                rebootRequestTimeString
        )
    }

    override fun uptimeRebooting(
        deviceId: String,
        rebootingTimeMillis: Long,
        rebootingTimeString: String
    ) {
        runBlocking {
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
    }

    override fun getUptime(deviceId: String): Uptime {
        val uptimeDto = dataSnapshotObject(
                runBlocking {
                    getValueFromDatabase(getAppDatabase().child(UPTIME_KEY).child(deviceId))
                },
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

    override fun sendIpAddressMap(deviceId: String, ipAddressMap: Map<String, String>) =
            runBlocking {
                setValueToDatabase(
                        getAppDatabase().child(UPTIME_KEY).child(deviceId).child(IP_ADDRESS_KEY),
                        serializeByGson(ipAddressMap)
                )
            }
}