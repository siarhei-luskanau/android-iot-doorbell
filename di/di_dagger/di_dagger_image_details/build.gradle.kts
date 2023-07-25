plugins {
    multiplatformConvention
    kotlin("kapt")
}

android.namespace = "siarhei.luskanau.iot.doorbell.dagger.imagedetails"

kotlin {
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(project(":common:common"))
                implementation(project(":data:dataDoorbellApi"))
                implementation(project(":di:di_dagger:di_dagger_common"))
                implementation(project(":ui:ui_common"))
                implementation(project(":ui:ui_image_details"))
                implementation(libs.android.material)
                implementation(libs.androidx.activity.ktx)
                implementation(libs.androidx.fragment.ktx)
                implementation(libs.androidx.navigation.ui.ktx)
                implementation(libs.dagger)
                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}

dependencies {
    kapt(libs.dagger.compiler)
}
