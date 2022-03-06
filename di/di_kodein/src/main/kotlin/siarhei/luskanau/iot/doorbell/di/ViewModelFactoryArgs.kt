package siarhei.luskanau.iot.doorbell.di

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

data class ViewModelFactoryArgs(
    val activity: FragmentActivity,
    val fragment: Fragment,
    val args: Bundle?
)
