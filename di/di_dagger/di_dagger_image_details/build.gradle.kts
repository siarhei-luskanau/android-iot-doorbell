plugins {
    id("multiplatformConvention")
    alias(libs.plugins.google.ksp)
}

kotlin {
    androidLibrary.namespace = "siarhei.luskanau.iot.doorbell.dagger.imagedetails"

    sourceSets {
        androidMain.dependencies {
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

dependencies {
    kspAndroid(libs.dagger.compiler)
}
