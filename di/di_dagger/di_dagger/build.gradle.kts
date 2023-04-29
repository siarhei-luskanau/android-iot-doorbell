plugins {
    multiplatformConvention
}

android.namespace = "siarhei.luskanau.iot.doorbell.di.dagger"

kotlin {
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(project(":base_camera"))
                implementation(project(":base_file"))
                implementation(project(":base_work_manager"))
                implementation(project(":common:common"))
                implementation(project(":data:dataDoorbellApi"))
                implementation(project(":data:dataDoorbellApiFirebase"))
                implementation(project(":di:di"))
                implementation(project(":di:di_dagger:di_dagger_common"))
                implementation(project(":di:di_dagger:di_dagger_doorbell_list"))
                implementation(project(":di:di_dagger:di_dagger_image_details"))
                implementation(project(":di:di_dagger:di_dagger_image_list"))
                implementation(project(":di:di_dagger:di_dagger_permissions"))
                implementation(project(":di:di_dagger:di_dagger_splash"))
                implementation(project(":navigation"))
                implementation(project(":ui:ui_doorbell_list"))
                implementation(project(":ui:ui_image_details"))
                implementation(project(":ui:ui_image_list"))
                implementation(project(":ui:ui_permissions"))
                implementation(project(":ui:ui_splash"))
                implementation(libs.android.material)
                implementation(libs.androidx.activity.ktx)
                implementation(libs.androidx.fragment.ktx)
                implementation(libs.androidx.work.runtime.ktx)
                implementation(libs.dagger)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.navigation.fragment.ktx)
                implementation(libs.timber)
            }
        }
    }
}
