plugins {
    id("multiplatformConvention")
    id("com.google.devtools.ksp")
}

android.namespace = "siarhei.luskanau.iot.doorbell.dagger.splash"

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(project(":di:di_dagger:di_dagger_common"))
            implementation(project(":ui:ui_common"))
            implementation(project(":ui:ui_splash"))
            implementation(libs.dagger)
        }
    }
}

dependencies {
    kspAndroid(libs.dagger.compiler)
}
