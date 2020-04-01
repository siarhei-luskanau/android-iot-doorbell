package siarhei.luskanau.iot.doorbell.common.test

import android.annotation.SuppressLint
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.spekframework.spek2.dsl.Root

@SuppressLint("RestrictedApi")
fun Root.setArchTaskExecutor() {
    beforeGroup {
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) = runnable.run()
            override fun postToMainThread(runnable: Runnable) = runnable.run()
            override fun isMainThread(): Boolean = true
        })
    }
    afterGroup {
        ArchTaskExecutor.getInstance().setDelegate(null)
    }
}

@ExperimentalCoroutinesApi
fun Root.setTestCoroutineScope(
    dispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
): CoroutineScope {
    val coroutineScope = TestCoroutineScope(dispatcher)
    beforeEachTest {
        Dispatchers.setMain(dispatcher)
    }
    afterEachTest {
        coroutineScope.cleanupTestCoroutines()
        Dispatchers.resetMain()
    }
    return coroutineScope
}
