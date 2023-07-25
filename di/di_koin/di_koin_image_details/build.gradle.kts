plugins {
    multiplatformConvention
}

android.namespace = "siarhei.luskanau.iot.doorbell.koin.imagedetails"

kotlin {
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(project(":di:di_koin:di_koin_common"))
                implementation(project(":data:dataDoorbellApi"))
                implementation(project(":common:common"))
                implementation(project(":ui:ui_common"))
                implementation(project(":ui:ui_image_details"))
                implementation(libs.android.material)
                implementation(libs.androidx.activity.ktx)
                implementation(libs.androidx.fragment.ktx)
                implementation(libs.androidx.navigation.ui.ktx)
                implementation(libs.koin.android)
                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}
