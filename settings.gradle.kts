rootProject.name = "Doorbell"

include(
        ":app:app_kodein",
        ":app:app_singleton",
        ":app:app_toothpick",

        ":app:dagger:app_dagger",
        ":app:dagger:dagger_common",
        ":app:dagger:dagger_doorbell_list",
        ":app:dagger:dagger_image_details",
        ":app:dagger:dagger_image_list",
        ":app:dagger:dagger_permissions",
        ":app:dagger:dagger_splash",

//        ":app:hilt:app_hilt",

        ":app:koin:app_koin",
        ":app:koin:koin_common",
        ":app:koin:koin_doorbell_list",
        ":app:koin:koin_image_details",
        ":app:koin:koin_image_list",
        ":app:koin:koin_permissions",
        ":app:koin:koin_splash",

        ":data:dataDoorbellApi",
        ":data:dataDoorbellApiFirebase",
        ":data:dataDoorbellApiStub",

        ":common:common",
        ":common:common_test",
        ":common:common_test_ui",

        ":base_android",
        ":base_camera",
        ":base_file",
        ":base_persistence",
        ":base_work_manager",
        ":base_cache",

        ":ui:ui_common",
        ":ui:ui_splash",
        ":ui:ui_permissions",
        ":ui:ui_doorbell_list",
        ":ui:ui_image_list",
        ":ui:ui_image_details",
        ":navigation",
)
