package siarhei.luskanau.iot.doorbell

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.startup.AppInitializer
import androidx.work.WorkerFactory
import siarhei.luskanau.iot.doorbell.navigation.OnActivityCreatedLifecycleCallbacks
import siarhei.luskanau.iot.doorbell.workmanager.WorkerFactoryProvider
import timber.log.Timber

class AppApplication : Application(), WorkerFactoryProvider {

    private val diHolder by lazy {
        AppInitializer.getInstance(this).initializeComponent(DiInitializer::class.java)
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        diHolder.onAppCreate(this)

        registerActivityLifecycleCallbacks(
            OnActivityCreatedLifecycleCallbacks {
                (it as? FragmentActivity?)?.let { fragmentActivity ->
                    fragmentActivity.supportFragmentManager.fragmentFactory =
                        diHolder.getFragmentFactory(fragmentActivity = fragmentActivity)
                }
            },
        )
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        diHolder.onAppTrimMemory(this)
    }

    override fun provideWorkerFactory(): WorkerFactory =
        diHolder.provideWorkerFactory()
}
