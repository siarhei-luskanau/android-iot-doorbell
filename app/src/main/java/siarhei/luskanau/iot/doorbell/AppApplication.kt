package siarhei.luskanau.iot.doorbell

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.work.Worker
import dagger.android.AndroidInjection
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import siarhei.luskanau.iot.doorbell.data.UptimeService
import siarhei.luskanau.iot.doorbell.data.model.AppBackgroundServices
import siarhei.luskanau.iot.doorbell.di.common.AppComponent
import siarhei.luskanau.iot.doorbell.di.common.DaggerAppComponent
import siarhei.luskanau.iot.doorbell.di.common.Injectable
import siarhei.luskanau.iot.doorbell.workmanager.dagger.HasWorkerInjector
import timber.log.Timber
import javax.inject.Inject

class AppApplication : DaggerApplication(), HasSupportFragmentInjector, HasWorkerInjector {

    @Inject
    lateinit var uptimeService: UptimeService
    @Inject
    lateinit var appBackgroundServices: AppBackgroundServices

    @Inject
    lateinit var workerInjector: DispatchingAndroidInjector<Worker>
    @Inject
    lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        init(this)

        uptimeService.startUptimeNotifications()
        appBackgroundServices.startServices()
    }

    override fun applicationInjector(): AppComponent = DaggerAppComponent
        .builder()
        .application(this)
        .build()

    override fun workerInjector() = workerInjector

    override fun supportFragmentInjector() = supportFragmentInjector

    companion object {

        fun init(application: AppApplication) {
            application
                .registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
                    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                        handleActivity(activity)
                    }

                    override fun onActivityStarted(activity: Activity) {
                    }

                    override fun onActivityResumed(activity: Activity) {
                    }

                    override fun onActivityPaused(activity: Activity) {
                    }

                    override fun onActivityStopped(activity: Activity) {
                    }

                    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
                    }

                    override fun onActivityDestroyed(activity: Activity) {
                    }
                })
        }

        fun handleActivity(activity: Activity) {
            if (activity is HasSupportFragmentInjector) {
                AndroidInjection.inject(activity)
            }

            (activity as? FragmentActivity)?.supportFragmentManager?.registerFragmentLifecycleCallbacks(
                object : FragmentManager.FragmentLifecycleCallbacks() {
                    override fun onFragmentCreated(
                        fragmentManager: FragmentManager,
                        fragment: Fragment,
                        savedInstanceState: Bundle?
                    ) {
                        if (fragment is Injectable) {
                            AndroidSupportInjection.inject(fragment)
                        }
                    }
                },
                true
            )
        }
    }
}
