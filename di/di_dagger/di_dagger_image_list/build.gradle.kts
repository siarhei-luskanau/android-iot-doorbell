plugins {
    multiplatformConvention
    kotlin("kapt")
}

android.namespace = "siarhei.luskanau.iot.doorbell.dagger.imagelist"

kotlin {
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(project(":common:common"))
                implementation(project(":data:dataDoorbellApi"))
                implementation(project(":di:di_dagger:di_dagger_common"))
                implementation(project(":ui:ui_common"))
                implementation(project(":ui:ui_image_list"))

                implementation(libs.android.material)
                implementation(libs.androidx.activity.ktx)
                implementation(libs.androidx.fragment.ktx)
                implementation(libs.androidx.paging.common.ktx)
                implementation(libs.androidx.paging.runtime.ktx)
                implementation(libs.androidx.work.runtime.ktx)
                implementation(libs.dagger)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.navigation.ui.ktx)
            }
        }
    }
}

dependencies {
    kapt(libs.dagger.compiler)
}
