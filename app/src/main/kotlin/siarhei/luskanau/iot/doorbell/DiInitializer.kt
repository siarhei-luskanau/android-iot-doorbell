package siarhei.luskanau.iot.doorbell

import android.content.Context
import androidx.startup.Initializer
import siarhei.luskanau.iot.doorbell.di.DiHolder
import siarhei.luskanau.iot.doorbell.di.DiHolderImpl

class DiInitializer : Initializer<DiHolder> {

    override fun create(context: Context): DiHolder = DiHolderImpl(context = context)

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
