package siarhei.luskanau.iot.doorbell

import android.arch.core.executor.ArchTaskExecutor
import android.arch.core.executor.TaskExecutor
import org.jetbrains.spek.api.Spek

object ArchTaskExecutorOverrideSpek : Spek({

    beforeGroup {
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) {
                runnable.run()
            }

            override fun postToMainThread(runnable: Runnable) {
                runnable.run()
            }

            override fun isMainThread(): Boolean = true
        })
    }

})
