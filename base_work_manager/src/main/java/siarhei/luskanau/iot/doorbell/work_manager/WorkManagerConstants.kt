package siarhei.luskanau.iot.doorbell.work_manager

import java.util.concurrent.TimeUnit

object WorkManagerConstants {

    const val REPEAT_INTERVAL = 15L
    val REPEAT_INTERVAL_TIME_UNIT = TimeUnit.MINUTES
    const val UPTIME_WORK_NAME = "UPTIME_WORK_NAME"

}