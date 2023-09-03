plugins {
    multiplatformConvention
    id("androidx.navigation.safeargs.kotlin")
}

android.namespace = "siarhei.luskanau.iot.doorbell.navigation"

kotlin {
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(project(":common:common"))
                implementation(project(":data:dataDoorbellApi"))
                implementation(project(":ui:ui_common"))
                implementation(project(":ui:ui_doorbell_list"))
                implementation(project(":ui:ui_image_details"))
                implementation(project(":ui:ui_image_list"))
                implementation(project(":ui:ui_permissions"))
                implementation(project(":ui:ui_splash"))
                implementation(libs.androidx.navigation.fragment.ktx)
                implementation(libs.androidx.navigation.ui.ktx)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.timber)
            }
        }
    }
}
