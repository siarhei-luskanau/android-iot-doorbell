package siarhei.luskanau.iot.doorbell.ui.doorbells.dagger

import dagger.Subcomponent

@Subcomponent(modules = arrayOf(DoorbellsModule::class))
class DoorbellsComponent {}
