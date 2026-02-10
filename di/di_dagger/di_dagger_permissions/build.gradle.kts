plugins {
    id("multiplatformConvention")
    id("com.google.devtools.ksp")
}

android.namespace = "siarhei.luskanau.iot.doorbell.dagger.permissions"

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(project(":di:di_dagger:di_dagger_common"))
            implementation(libs.dagger)
        }
    }
}

dependencies {
    kspAndroid(libs.dagger.compiler)
}
