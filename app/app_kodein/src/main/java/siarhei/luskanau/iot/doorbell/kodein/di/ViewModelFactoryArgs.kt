package siarhei.luskanau.iot.doorbell.kodein.di

import android.os.Bundle
import androidx.fragment.app.Fragment
import siarhei.luskanau.iot.doorbell.common.AppNavigation

data class ViewModelFactoryArgs(
    val appNavigation: AppNavigation,
    val fragment: Fragment,
    val args: Bundle?
)
