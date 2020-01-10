package siarhei.luskanau.iot.doorbell.navigation

import android.app.Activity
import android.app.Application
import android.os.Bundle

open class OnActivityCreatedLifecycleCallbacks(
    private val callbacks: (Activity) -> Unit
) : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        callbacks.invoke(activity)
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }
}
