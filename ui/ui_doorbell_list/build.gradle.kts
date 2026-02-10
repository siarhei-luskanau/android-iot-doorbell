plugins {
    id("multiplatformConvention")
}

android.namespace = "siarhei.luskanau.iot.doorbell.ui.doorbelllist"

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.androidx.paging.compose)
        }
        androidMain.dependencies {
            implementation(project(":common:common"))
            implementation(project(":data:dataDoorbellApi"))
            implementation(project(":ui:ui_common"))
            implementation(libs.androidx.paging.runtime.ktx)
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}
