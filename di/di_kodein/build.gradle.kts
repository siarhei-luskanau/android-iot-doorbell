plugins {
    id("multiplatformConvention")
}

kotlin {
    androidLibrary.namespace = "siarhei.luskanau.iot.doorbell.di.kodein"

    sourceSets {
        commonMain.dependencies {
            implementation(libs.androidx.paging.compose)
        }
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
            implementation(project(":navigation"))
            implementation(project(":ui:ui_common"))
            implementation(project(":ui:ui_doorbell_list"))
            implementation(project(":ui:ui_image_details"))
            implementation(project(":ui:ui_image_list"))
            implementation(project(":ui:ui_permissions"))
            implementation(project(":ui:ui_splash"))
            implementation(libs.androidx.paging.runtime.ktx)
            implementation(libs.androidx.startup.runtime)
            implementation(libs.androidx.work.runtime.ktx)
            implementation(libs.kodein.android.core)
            implementation(libs.kotlin.reflect)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.timber)
        }
    }
}
