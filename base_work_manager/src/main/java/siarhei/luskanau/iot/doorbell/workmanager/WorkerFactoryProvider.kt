package siarhei.luskanau.iot.doorbell.workmanager

import androidx.work.WorkerFactory

interface WorkerFactoryProvider {
    fun provideWorkerFactory(): WorkerFactory
}
