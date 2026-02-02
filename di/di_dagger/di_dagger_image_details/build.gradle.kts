plugins {
    id("multiplatformDaggerConvention")
    id("com.google.devtools.ksp")
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
                implementation(libs.dagger)
                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}

dependencies {
    kspAndroid(libs.dagger.compiler)
}
