package siarhei.luskanau.iot.doorbell

import android.app.Service
import android.content.Intent
import android.os.IBinder
import timber.log.Timber

class CameraService : Service() {

    override fun onCreate() {
        super.onCreate()
        Timber.d("CameraService: onCreate()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("CameraService: onDestroy()")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        return Service.START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

}
