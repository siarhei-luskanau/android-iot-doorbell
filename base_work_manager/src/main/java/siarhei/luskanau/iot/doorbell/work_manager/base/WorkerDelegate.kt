package siarhei.luskanau.iot.doorbell.work_manager.base

import androidx.work.Worker

interface WorkerDelegate {
    fun doWork(): Worker.Result
}