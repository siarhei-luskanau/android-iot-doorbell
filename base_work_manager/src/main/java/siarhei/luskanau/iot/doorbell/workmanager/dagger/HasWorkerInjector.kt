package siarhei.luskanau.iot.doorbell.workmanager.dagger

import androidx.work.Worker
import dagger.android.AndroidInjector

interface HasWorkerInjector {
    fun workerInjector(): AndroidInjector<Worker>
}