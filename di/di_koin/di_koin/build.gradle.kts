plugins {
    id("multiplatformConvention")
}

android.namespace = "siarhei.luskanau.iot.doorbell.di.koin"

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(project(":base_android"))
            implementation(project(":base_camera"))
            implementation(project(":base_file"))
            implementation(project(":base_work_manager"))
            implementation(project(":common:common"))
            implementation(project(":data:dataDoorbellApi"))
            implementation(project(":data:dataDoorbellApiFirebase"))
            implementation(project(":data:dataDoorbellApiStub"))
            implementation(project(":di:di"))
            implementation(project(":di:di_koin:di_koin_common"))
            implementation(project(":di:di_koin:di_koin_doorbell_list"))
            implementation(project(":di:di_koin:di_koin_image_details"))
            implementation(project(":di:di_koin:di_koin_image_list"))
            implementation(project(":di:di_koin:di_koin_permissions"))
            implementation(project(":di:di_koin:di_koin_splash"))
            implementation(project(":navigation"))
            implementation(project(":ui:ui_common"))
            implementation(project(":ui:ui_doorbell_list"))
            implementation(project(":ui:ui_image_details"))
            implementation(project(":ui:ui_image_list"))
            implementation(project(":ui:ui_permissions"))
            implementation(project(":ui:ui_splash"))
            implementation(libs.androidx.startup.runtime)
            implementation(libs.androidx.work.runtime.ktx)
            implementation(libs.koin.android)
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}
