package siarhei.luskanau.iot.doorbell.workmanager.dagger

import androidx.work.Worker
import dagger.MapKey
import kotlin.reflect.KClass

@MapKey
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class WorkerKey(val value: KClass<out Worker>)