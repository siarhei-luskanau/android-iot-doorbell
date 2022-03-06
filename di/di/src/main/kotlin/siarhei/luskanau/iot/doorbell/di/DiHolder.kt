package siarhei.luskanau.iot.doorbell.di

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import androidx.work.WorkerFactory

interface DiHolder {
    fun onAppCreate(application: Application)
    fun onAppTrimMemory(application: Application)
    fun getFragmentFactory(fragmentActivity: FragmentActivity): FragmentFactory
    fun provideWorkerFactory(): WorkerFactory
}
