rootProject.name = "Doorbell"

pluginManagement {
    includeBuild("convention-plugin-multiplatform")
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

include(
    ":app",
    ":base_android",
    ":base_camera",
    ":base_file",
    ":base_work_manager",
    ":common:common",
    ":common:common_test_ui",
    ":data:dataDoorbellApi",
    ":data:dataDoorbellApiFirebase",
    ":data:dataDoorbellApiStub",
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
    ":di:di_manual",
    ":navigation",
    ":ui:ui_common",
    ":ui:ui_doorbell_list",
    ":ui:ui_image_details",
    ":ui:ui_image_list",
    ":ui:ui_permissions",
    ":ui:ui_splash"
)
