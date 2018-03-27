package siarhei.luskanau.iot.doorbell.data.repository

import io.reactivex.Completable
import io.reactivex.Flowable
import siarhei.luskanau.iot.doorbell.data.model.Uptime

interface UptimeRepository {

    fun uptimeStartup(
            deviceId: String,
            startupTimeMillis: Long,
            startupTimeString: String
    ): Completable

    fun uptimePing(
            deviceId: String,
            pingTimeMillis: Long,
            pingTimeString: String
    ): Completable

    fun uptimeRebootRequest(
            deviceId: String,
            rebootRequestTimeMillis: Long,
            rebootRequestTimeString: String
    ): Completable

    fun uptimeRebooting(
            deviceId: String,
            rebootingTimeMillis: Long,
            rebootingTimeString: String
    ): Completable

    fun listenUptime(deviceId: String): Flowable<Uptime>

    fun sendIpAddressMap(deviceId: String, ipAddressMap: Map<String, String>): Completable

}