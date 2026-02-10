plugins {
    id("multiplatformConvention")
}

android.namespace = "siarhei.luskanau.iot.doorbell.koin.imagelist"

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.androidx.paging.compose)
        }
        androidMain.dependencies {
            implementation(project(":common:common"))
            implementation(project(":data:dataDoorbellApi"))
            implementation(project(":di:di_koin:di_koin_common"))
            implementation(project(":ui:ui_common"))
            implementation(project(":ui:ui_image_list"))
            implementation(libs.androidx.paging.runtime.ktx)
            implementation(libs.androidx.work.runtime.ktx)
            implementation(libs.koin.android)
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}
