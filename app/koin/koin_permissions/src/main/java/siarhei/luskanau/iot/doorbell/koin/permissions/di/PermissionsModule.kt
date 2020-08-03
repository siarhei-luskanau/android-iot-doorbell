package siarhei.luskanau.iot.doorbell.koin.permissions.di

import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import siarhei.luskanau.iot.doorbell.ui.permissions.PermissionsFragment
import siarhei.luskanau.iot.doorbell.ui.permissions.PermissionsPresenter

val permissionsModule = module {

    factory { (appNavigation: AppNavigation) ->
        PermissionsFragment { get { parametersOf(appNavigation) } }
    }

    factory { (appNavigation: AppNavigation) ->
        PermissionsPresenter(appNavigation)
    }
}
