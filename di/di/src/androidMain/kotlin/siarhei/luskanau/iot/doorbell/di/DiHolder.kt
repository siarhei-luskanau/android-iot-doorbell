package siarhei.luskanau.iot.doorbell.di

import android.app.Application
import androidx.work.WorkerFactory

interface DiHolder {
    fun onAppCreate(application: Application)
    fun onAppTrimMemory(application: Application)
    fun provideWorkerFactory(): WorkerFactory
}
