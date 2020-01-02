package siarhei.luskanau.iot.doorbell.dagger

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory

interface HasFragmentFactory {
    fun buildFragmentFactory(fragmentActivity: FragmentActivity): FragmentFactory
}
