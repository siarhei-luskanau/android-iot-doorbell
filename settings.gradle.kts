rootProject.name = "Doorbell"

include(":app:app_kodein")
include(":app:app_singleton")
include(":app:app_toothpick")

include(":app:dagger:app_dagger")
include(":app:dagger:dagger_common")
include(":app:dagger:dagger_doorbell_list")
include(":app:dagger:dagger_image_details")
include(":app:dagger:dagger_image_list")
include(":app:dagger:dagger_permissions")
include(":app:dagger:dagger_splash")

//include(":app:hilt:app_hilt")

include(":app:koin:app_koin")
include(":app:koin:koin_common")
include(":app:koin:koin_doorbell_list")
include(":app:koin:koin_image_details")
include(":app:koin:koin_image_list")
include(":app:koin:koin_permissions")
include(":app:koin:koin_splash")

include(":data:dataDoorbellApi")
include(":data:dataDoorbellApiFirebase")
include(":data:dataDoorbellApiStub")

include(":common:common")
include(":common:common_test")
include(":common:common_test_ui")

include(":base_android")
include(":base_camera")
include(":base_file")
include(":base_persistence")
include(":base_work_manager")
include(":base_cache")

include(":ui:ui_common")
include(":ui:ui_splash")
include(":ui:ui_permissions")
include(":ui:ui_doorbell_list")
include(":ui:ui_image_list")
include(":ui:ui_image_details")
include(":navigation")
