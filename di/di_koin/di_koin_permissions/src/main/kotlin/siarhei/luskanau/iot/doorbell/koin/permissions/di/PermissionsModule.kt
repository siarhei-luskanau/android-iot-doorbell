package siarhei.luskanau.iot.doorbell.koin.permissions.di

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import siarhei.luskanau.iot.doorbell.koin.common.di.fragment
import siarhei.luskanau.iot.doorbell.ui.permissions.PermissionsFragment
import siarhei.luskanau.iot.doorbell.ui.permissions.PermissionsPresenter

val permissionsModule = module {

    fragment { activity: FragmentActivity ->
        PermissionsFragment { fragment: Fragment ->
            get { parametersOf(activity, fragment, fragment.arguments) }
        }
    }

    factory { (activity: FragmentActivity, _: Fragment, _: Bundle?) ->
        PermissionsPresenter(
            appNavigation = get { parametersOf(activity) },
        )
    }
}
