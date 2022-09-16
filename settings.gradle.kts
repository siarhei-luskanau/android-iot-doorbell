rootProject.name = "Doorbell"

include(
    ":data:dataDoorbellApi",
    ":data:dataDoorbellApiFirebase",
    ":data:dataDoorbellApiStub",

    ":common:common",
    ":common:common_test_ui",

    ":base_android",
    ":base_camera",
    ":base_file",
    ":base_persistence",
    ":base_work_manager",

    ":ui:ui_common",
    ":ui:ui_splash",
    ":ui:ui_permissions",
    ":ui:ui_doorbell_list",
    ":ui:ui_image_list",
    ":ui:ui_image_details",
    ":navigation",

    ":di:di",
    ":di:di_dagger:di_dagger",
    ":di:di_dagger:di_dagger_common",
    ":di:di_dagger:di_dagger_doorbell_list",
    ":di:di_dagger:di_dagger_image_details",
    ":di:di_dagger:di_dagger_image_list",
    ":di:di_dagger:di_dagger_permissions",
    ":di:di_dagger:di_dagger_splash",
    ":di:di_kodein",
    ":di:di_koin:di_koin",
    ":di:di_koin:di_koin_common",
    ":di:di_koin:di_koin_doorbell_list",
    ":di:di_koin:di_koin_image_details",
    ":di:di_koin:di_koin_image_list",
    ":di:di_koin:di_koin_permissions",
    ":di:di_koin:di_koin_splash",
    ":di:di_singleton",
    ":di:di_toothpick",

    ":app",
)
