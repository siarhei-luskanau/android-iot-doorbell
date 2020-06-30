package siarhei.luskanau.iot.doorbell.kodein.di

import android.os.Bundle
import siarhei.luskanau.iot.doorbell.common.AppNavigation

data class ViewModelFactoryArgs(
    val appNavigation: AppNavigation,
    val args: Bundle?
)
