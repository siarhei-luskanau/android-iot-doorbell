package siarhei.luskanau.iot.doorbell.data.repository

import com.google.gson.Gson
import durdinapps.rxfirebase2.RxFirebaseDatabase
import io.reactivex.Completable
import io.reactivex.Flowable
import siarhei.luskanau.iot.doorbell.data.model.Uptime

class UptimeFirebaseRepository(
        override val gson: Gson
) : BaseFirebaseRepository(gson), UptimeRepository {

    companion object {
        private const val UPTIME_KEY = "uptime"
        private const val IP_ADDRESS_KEY = "ip_address"
    }

    override fun uptimeStartup(
            deviceId: String,
            startupTimeMillis: Long,
            startupTimeString: String
    ): Completable = Completable.mergeArray(
            RxFirebaseDatabase
                    .setValue(
                            getAppDatabase().child(UPTIME_KEY).child(deviceId)
                                    .child(Uptime.STARTUP_TIME_MILLIS_KEY),
                            startupTimeMillis
                    ),
            RxFirebaseDatabase
                    .setValue(
                            getAppDatabase().child(UPTIME_KEY).child(deviceId)
                                    .child(Uptime.STARTUP_TIME_STRING_KEY),
                            startupTimeString
                    )
    )

    override fun uptimePing(
            deviceId: String,
            pingTimeMillis: Long,
            pingTimeString: String
    ): Completable = Completable.mergeArray(
            RxFirebaseDatabase
                    .setValue(
                            getAppDatabase().child(UPTIME_KEY).child(deviceId)
                                    .child(Uptime.PING_TIME_MILLIS_KEY),
                            pingTimeMillis
                    ),
            RxFirebaseDatabase
                    .setValue(
                            getAppDatabase().child(UPTIME_KEY).child(deviceId)
                                    .child(Uptime.PING_TIME_STRING_KEY),
                            pingTimeString
                    )
    )

    override fun uptimeRebootRequest(
            deviceId: String,
            rebootRequestTimeMillis: Long,
            rebootRequestTimeString: String
    ): Completable = Completable.mergeArray(
            RxFirebaseDatabase
                    .setValue(
                            getAppDatabase().child(UPTIME_KEY).child(deviceId)
                                    .child(Uptime.REBOOT_REQUEST_TIME_MILLIS_KEY),
                            rebootRequestTimeMillis
                    ),
            RxFirebaseDatabase
                    .setValue(
                            getAppDatabase().child(UPTIME_KEY).child(deviceId)
                                    .child(Uptime.REBOOT_REQUEST_TIME_STRING_KEY),
                            rebootRequestTimeString
                    )
    )

    override fun uptimeRebooting(
            deviceId: String,
            rebootingTimeMillis: Long,
            rebootingTimeString: String
    ): Completable = Completable.mergeArray(
            RxFirebaseDatabase
                    .setValue(
                            getAppDatabase().child(UPTIME_KEY).child(deviceId)
                                    .child(Uptime.REBOOTING_TIME_MILLIS_KEY),
                            rebootingTimeMillis
                    ),
            RxFirebaseDatabase
                    .setValue(
                            getAppDatabase().child(UPTIME_KEY).child(deviceId)
                                    .child(Uptime.REBOOTING_TIME_STRING_KEY),
                            rebootingTimeString
                    )
    )

    override fun listenUptime(deviceId: String): Flowable<Uptime> = RxFirebaseDatabase
            .observeValueEvent(
                    getAppDatabase().child(UPTIME_KEY).child(deviceId)
            )
            .map { dataSnapshotObject(it, Uptime::class.java) }

    override fun sendIpAddressMap(deviceId: String, ipAddressMap: Map<String, String>): Completable = RxFirebaseDatabase
            .setValue(
                    getAppDatabase().child(UPTIME_KEY).child(deviceId).child(IP_ADDRESS_KEY),
                    serializeByGson(ipAddressMap)
            )

}