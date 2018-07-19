package siarhei.luskanau.iot.doorbell.work_manager.dagger

import androidx.work.Worker
import dagger.android.AndroidInjector

interface HasWorkerInjector {
    fun workerInjector(): AndroidInjector<Worker>
}